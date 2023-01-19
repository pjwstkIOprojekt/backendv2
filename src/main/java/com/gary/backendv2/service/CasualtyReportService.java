package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.factories.impl.CasualtyReportFactory;
import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.Consumption;
import com.gary.backendv2.model.ConsumptionOfMaterials;
import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.CasualtyReportRequest;
import com.gary.backendv2.model.dto.response.CasualtyReportResponse;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.inventory.Inventory;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.repository.CasualtyReportRepository;
import com.gary.backendv2.repository.ConsumptionOfMaterialsRepository;
import com.gary.backendv2.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CasualtyReportService {

    @Autowired
    private final CasualtyReportRepository casualtyReportRepository;

    private Map<String, Inventory> stateOfInventory;

    private final FacilityRepository facilityRepository;
    @Autowired
    private final ConsumptionOfMaterialsRepository consumptionOfMaterialsRepository;

    public void generateCasualtyReport(Incident incident) {
        ThreadLocal<Map<String, Inventory>> stateOfInventory = new ThreadLocal<>();
        stateOfInventory.set(incident.getAmbulances().stream().collect(Collectors.toMap(Ambulance::getLicensePlate, Ambulance::getInventory)));
        this.stateOfInventory = stateOfInventory.get();
        CasualtyReportFactory casualtyReportFactory = new CasualtyReportFactory();
        List<CasualtyReport> reportsToGenerate = new ArrayList<>();
        for (int i = 0; i < incident.getVictimCount(); i++ ) {
            reportsToGenerate.add(casualtyReportFactory.create(incident));
        }
        casualtyReportRepository.saveAll(reportsToGenerate);
        }

    public void updateStateOfInventory(Incident incident) {
        for (Map.Entry<String, Inventory> entry : stateOfInventory.entrySet()) {
            String licensePlate = entry.getKey();
            Inventory originalInventory = entry.getValue();
            Ambulance ambulance = incident.getAmbulances().stream().filter(a -> a.getLicensePlate().equals(licensePlate)).findFirst().orElse(null);
            if (ambulance == null) {
                throw new HttpException(HttpStatus.NOT_FOUND, "Ambulance not found");
            }
            Inventory currentInventory = ambulance.getInventory();
            List<CasualtyReport> reports = casualtyReportRepository.findByIncident(incident);
            reports.removeIf(report -> report.getIncident().getAmbulances().stream().noneMatch(x -> x.getLicensePlate().equals(licensePlate)));

            for (CasualtyReport report : reports) {
                ConsumptionOfMaterials consumptionOfMaterials = new ConsumptionOfMaterials();
                consumptionOfMaterials.setCasualtyReport(report);
                for (Map.Entry<Integer, ItemContainer> itemEntry : originalInventory.getItems().entrySet()) {
                    Integer itemId = itemEntry.getKey();
                    ItemContainer originalContainer = itemEntry.getValue();
                    ItemContainer currentContainer = currentInventory.getItems().get(itemId);
                    if (currentContainer != null && originalContainer != null) {
                        //update the current count of container by subtracting the original count from current count
                        int count = currentContainer.getCount() - originalContainer.getCount();
                        if (count > 0) {
                            Consumption consumption = new Consumption();
                            consumption.setCount(count);
                            consumption.setItemId(itemId);
                            consumption.setConsumptionOfMaterials(consumptionOfMaterials);
                            if (consumptionOfMaterials.getItems() == null) {
                                consumptionOfMaterials.setItems(new HashMap<>());
                            }
                            consumptionOfMaterials.getItems().put(itemId, consumption);
                        }
                    }
                }
                if(consumptionOfMaterials.getItems() != null && !consumptionOfMaterials.getItems().isEmpty()){
                    consumptionOfMaterialsRepository.save(consumptionOfMaterials);
                }
            }
        }
    }

    public void addDiscription(Integer id, String description) {
        Optional<CasualtyReport> optionalCasualtyReport = casualtyReportRepository.findById(id);
        if(optionalCasualtyReport.isEmpty()){
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Casualty report with id %s not found", id));
        }
        CasualtyReport casualtyReport = optionalCasualtyReport.get();
        casualtyReport.setDescription(description);
        casualtyReportRepository.save(casualtyReport);
    }

    public void addFacility(Integer id, Integer facilityId){
        Optional<Facility> optionalFacility = facilityRepository.findById(id);
        if(optionalFacility.isEmpty()){
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Facility with id %s not found", id));
        }
        Facility facility = optionalFacility.get();
        Optional<CasualtyReport> optionalCasualtyReport = casualtyReportRepository.findById(facilityId);
        if(optionalCasualtyReport.isEmpty()){
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Casualty report with id %s not found", facilityId));
        }
        CasualtyReport casualtyReport = optionalCasualtyReport.get();
        casualtyReport.getFacilities().add(facility);
        facility.setCasualtyReport(casualtyReport);
        casualtyReportRepository.save(casualtyReport);
        facilityRepository.save(facility);
    }

    public List<CasualtyReportResponse> getAllCasualtyReports() {
        List<CasualtyReport> casualtyReports = casualtyReportRepository.findAll();
        List<CasualtyReportResponse> allreports = new ArrayList<>();
        for (CasualtyReport casualtyReport: casualtyReports) {
            allreports.add(CasualtyReportResponse.builder()
                            .description(casualtyReport.getDescription())
                            .facilities(casualtyReport.getFacilities())
                            .itemCounts(casualtyReport.getItemCounts())
                            .incident(casualtyReport.getIncident())
                            .build());
        }
        return allreports;
    }
    public CasualtyReportResponse getCasualtyReportById(Integer id) {
        Optional<CasualtyReport> optionalCasualtyReport = casualtyReportRepository.findById(id);
        if(optionalCasualtyReport.isEmpty()){
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find casualty report with %s", id));
        }
        CasualtyReport report = optionalCasualtyReport.get();
        return CasualtyReportResponse.builder()
                .incident(report.getIncident())
                .description(report.getDescription())
                .facilities(report.getFacilities())
                .itemCounts(report.getItemCounts())
                .build();
    }

    public void updateDescription(Integer id, CasualtyReportRequest casualtyReportRequest) {
        Optional<CasualtyReport> optionalCasualtyReport = casualtyReportRepository.findById(id);
        if(optionalCasualtyReport.isEmpty()){
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find casualty report with %s", id));
        }
        CasualtyReport report = optionalCasualtyReport.get();
        report.setDescription(casualtyReportRequest.getDescription());
        casualtyReportRepository.save(report);
    }

    public void deleteCasualtyReport(Integer id) {
        Optional<CasualtyReport> optionalCasualtyReport = casualtyReportRepository.findById(id);
        if(optionalCasualtyReport.isEmpty()){
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find casualty report with %s", id));
        }
        CasualtyReport report = optionalCasualtyReport.get();
        casualtyReportRepository.delete(report);
    }


}