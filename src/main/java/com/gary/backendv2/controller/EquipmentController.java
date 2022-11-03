package com.gary.backendv2.controller;

import com.gary.backendv2.model.Equipment;
import com.gary.backendv2.model.dto.request.EquipmentRequest;
import com.gary.backendv2.model.dto.response.EquipmentResponse;
import com.gary.backendv2.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping("")
    public List<EquipmentResponse> getEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{equipmentId}")
    public EquipmentResponse getEquipmentById(@PathVariable Integer equipmentId) {
        return equipmentService.getEquipmentById(equipmentId);
    }

    @DeleteMapping("/{equipmentId}")
    public void deleteEquipment(@PathVariable Integer equipmentId){
        equipmentService.deleteEquipment(equipmentId);
    }

    @PutMapping("/{equipmentId}")
    public void updateEquipment(@PathVariable Integer equipmentId, @RequestBody EquipmentRequest equipment){
        equipmentService.updateEquipment(equipmentId,equipment);
    }

    @PostMapping("")
    public void addEquipment(EquipmentRequest equipmentRequest) {
        equipmentService.addEquipment(equipmentRequest);
    }


}
