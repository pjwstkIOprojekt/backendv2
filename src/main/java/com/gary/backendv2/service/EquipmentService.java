package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Ambulance;
import com.gary.backendv2.model.Equipment;
import com.gary.backendv2.model.EquipmentInAmbulance;
import com.gary.backendv2.model.dto.request.EquipmentRequest;
import com.gary.backendv2.model.dto.response.EquipmentResponse;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.EquipmentInAmbulanceRepository;
import com.gary.backendv2.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final AmbulanceRepository ambulanceRepository;

    private final EquipmentInAmbulanceRepository equipmentInAmbulanceRepository;

    public List<EquipmentResponse> getAllEquipment() {
        List<Equipment> equipment = equipmentRepository.findAll();
        List<EquipmentResponse> equipmentResponses = new ArrayList<>();
        for (Equipment e : equipment) {
            equipmentResponses.add(
                    EquipmentResponse
                            .builder()
                            .equipmentId(e.getEquipmentId())
                            .equipmentType(e.getEquipmentType())
                            .name(e.getName())
                            .date(e.getDate())
                            .build()
            );
        }
        return equipmentResponses;
    }

    public EquipmentResponse getEquipmentById(Integer equipmentId) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(equipmentId);
        if (optionalEquipment.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find equipment with %s", equipmentId));
        }
        Equipment e = optionalEquipment.get();
        return EquipmentResponse.builder()
                .equipmentId(e.getEquipmentId())
                .equipmentType(e.getEquipmentType())
                .name(e.getName())
                .date(e.getDate())
                .build();
    }

    public void deleteEquipment(Integer equipmentId) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(equipmentId);
        if (optionalEquipment.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find disease with %s", equipmentId));
        }
        Equipment equipment = optionalEquipment.get();
        equipmentRepository.delete(equipment);
    }

    public void updateEquipment(Integer equipmentId, EquipmentRequest equipmentRequest){
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(equipmentId);
        if (optionalEquipment.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find disease with %s", equipmentId));
        }

        Equipment equipment = optionalEquipment.get();
        equipment.setEquipmentType(equipmentRequest.getEquipmentType());
        equipment.setName(equipmentRequest.getName());
        equipment.setDate(equipmentRequest.getDate());
        equipmentRepository.save(equipment);

    }

    public void addEquipment(EquipmentRequest equipmentRequest) {
        Equipment equipment = Equipment.builder()
                .equipmentType(equipmentRequest.getEquipmentType())
                .name(equipmentRequest.getName())
                .date(equipmentRequest.getDate())
                .build();
        equipmentRepository.save(equipment);
    }

    public Set<EquipmentInAmbulance> getEquipmentInAmbulance(String licancePlate) {
        Optional<Ambulance> optionalAmbulance = ambulanceRepository.findByLicensePlate(licancePlate);
        if (optionalAmbulance.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find ambulance with %s", licancePlate));
        }
        Ambulance ambulance = optionalAmbulance.get();
        return ambulance.getEquipmentInAmbulances();
    }


    public void addEquipmentToAmbulance(String licencePlate, Integer equipmentId, EquipmentInAmbulance equipmentInAmbulance) {
        Optional<Ambulance> optionalAmbulance = ambulanceRepository.findByLicensePlate(licencePlate);
        if (optionalAmbulance.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find ambulance with %s", licencePlate));
        }
        Ambulance ambulance = optionalAmbulance.get();

        Optional<Equipment> optionalEquipment = equipmentRepository.findById(equipmentId);
        if (optionalEquipment.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find disease with %s", equipmentId));
        }
        Equipment equipment = optionalEquipment.get();

        EquipmentInAmbulance eq = EquipmentInAmbulance
                .builder()
                .equipment(equipment)
                .ambulance(ambulance)
                .date(equipmentInAmbulance.getDate())
                .amount(equipmentInAmbulance.getAmount())
                .usage(equipmentInAmbulance.getUsage())
                .waste(equipmentInAmbulance.getWaste())
                .unitsOfMeasure(equipmentInAmbulance.getUnitsOfMeasure())
                .comments(equipmentInAmbulance.getComments())
                .build();
        equipmentInAmbulanceRepository.save(eq);
    }
}
