package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.UpdateAmbulanceStateRequest;
import com.gary.backendv2.service.AmbulanceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ambulance")
@RequiredArgsConstructor
public class AmbulanceController {
    private final AmbulanceService ambulanceService;

    @GetMapping
    public ResponseEntity<?> getAllAmbulances() {
        return ResponseEntity.ok(ambulanceService.getAllAmbulances());
    }

    @GetMapping("/{licensePlate}")
    public ResponseEntity<?> getAmbulanceByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getAmbulanceByLicensePlate(licensePlate));
    }

    @GetMapping("/{licensePlate}/history")
    public ResponseEntity<?> getAmbulanceHistory(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getAmbulanceHistory(licensePlate));
    }

    @GetMapping("/{licensePlate}/state")
    public ResponseEntity<?> getCurrentAmbulanceState(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getAmbulanceCurrentState(licensePlate));
    }

    @PostMapping("/{licensePlate}/state")
    public ResponseEntity<?> changeAmbulanceState(@PathVariable String licensePlate, @RequestBody @Valid UpdateAmbulanceStateRequest changeStateRequest) {
        return ResponseEntity.ok(ambulanceService.changeAmbulanceState(licensePlate, changeStateRequest));
    }

    @PostMapping
    public ResponseEntity<?> addAmbulance(@RequestBody @Valid AddAmbulanceRequest addAmbulanceRequest) {
        return ResponseEntity.ok(ambulanceService.addAmbulance(addAmbulanceRequest));
    }

    @PutMapping
    public void updateAmbulance(@RequestBody @Valid AddAmbulanceRequest addAmbulanceRequest) {
        ambulanceService.updateAmbulance(addAmbulanceRequest);
    }

    @DeleteMapping("/{licensePlate}")
    public void deleteAmbulance(@PathVariable String licensePlate) {
        ambulanceService.deleteAmbulance(licensePlate);
    }

    @GetMapping("/{licensePlate}/inventory")
    public ResponseEntity<?> getInventory(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getInventory(licensePlate));
    }

}
