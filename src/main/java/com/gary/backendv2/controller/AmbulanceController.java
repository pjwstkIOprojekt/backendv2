package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.PostAmbulanceLocationRequest;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.service.AmbulanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
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

    @GetMapping("/{licensePlate}/location/path")
    public ResponseEntity<?> getAmbulancePath(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getAmbulancePath(licensePlate));
    }

    @GetMapping("/{licensePlate}/equipment")
    public ResponseEntity<?> getItems(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getItems(licensePlate));
    }

    @Operation(summary = "Adds items to the ambulance if count not provided adds one")
    @PostMapping("/{licensePlate}/items/add/{itemId}")
    public void addItem(@PathVariable String licensePlate, @PathVariable Integer itemId, @RequestParam(required = false) Integer count) {
        if (count > 1) {
            ambulanceService.addItems(licensePlate, itemId, count);
        } else ambulanceService.addItem(licensePlate, itemId);
    }

    @PostMapping("/{licensePlate}/state/{state}")
    public ResponseEntity<?> changeAmbulanceState(@PathVariable String licensePlate, @PathVariable AmbulanceStateType state) {
        return ResponseEntity.ok(ambulanceService.changeAmbulanceState(licensePlate, state));
    }

    @PostMapping
    public ResponseEntity<?> addAmbulance(@RequestBody @Valid AddAmbulanceRequest addAmbulanceRequest) {
        return ResponseEntity.ok(ambulanceService.addAmbulance(addAmbulanceRequest));
    }

    @PostMapping("/{licensePlate}/location")
    public void postLocation(@PathVariable String licensePlate, @Valid @RequestBody PostAmbulanceLocationRequest postAmbulanceLocationRequest) {
        ambulanceService.addGeoLocation(licensePlate, postAmbulanceLocationRequest);
    }

    @PutMapping
    public void updateAmbulance(@RequestBody @Valid AddAmbulanceRequest addAmbulanceRequest) {
        ambulanceService.updateAmbulance(addAmbulanceRequest);
    }

    @DeleteMapping("/{licensePlate}")
    public void deleteAmbulance(@PathVariable String licensePlate) {
        ambulanceService.deleteAmbulance(licensePlate);
    }

    @Operation(summary = "Removes item from an ambulance. If count is not provided removes one item")
    @DeleteMapping("/{licensePlate}/items/remove/{itemId}")
    public void removeItem(@PathVariable String licensePlate, @PathVariable Integer itemId, @RequestParam(required = false) Integer count) {
        throw new NotImplementedException();
    }

    @Operation(summary = "Removes all items of given id from an ambulance")
    @DeleteMapping("/{licensePlate}/items/remove/{itemId}/all")
    public void removeAllItemsOfId(
            @PathVariable String licensePlate,
            @PathVariable Integer itemId) {
        throw new NotImplementedException();
    }
}
