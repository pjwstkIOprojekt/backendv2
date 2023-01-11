package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.factories.impl.CasualtyReportFactory;
import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.Consumption;
import com.gary.backendv2.model.ConsumptionOfMaterials;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.inventory.Inventory;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.repository.CasualtyReportRepository;
import com.gary.backendv2.repository.ConsumptionOfMaterialsRepository;
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
            List<CasualtyReport> reports = casualtyReportRepository.findByIncidentAndAmbulanceLicensePlate(incident, licensePlate);
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


}
