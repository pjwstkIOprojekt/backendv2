package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.PostAmbulanceLocationRequest;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.service.AmbulanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/{licensePlate}/incident")
    public ResponseEntity<?> currentIncident(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getCurrentIncident(licensePlate));
    }

    @GetMapping("/{licensePlate}/crew")
    public ResponseEntity<?> getAllAssignedMedics(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ambulanceService.getCrewMedics(licensePlate));
    }

    @Operation(summary = "Adds items to the ambulance if count not provided adds one")
    @PostMapping("/{licensePlate}/items/add/{itemId}")
    public void addItem(@PathVariable String licensePlate, @PathVariable Integer itemId, @RequestParam(required = false) Integer count) {
        if (count != null && count > 1) {
            ambulanceService.addItems(licensePlate, itemId, count);
        } else ambulanceService.addItem(licensePlate, itemId);
    }

    @Operation(summary = "Change unit of an item")
    @PutMapping("/{licensePlate}/items/add/{itemId}")
    public void editItemUnit(@PathVariable String licensePlate, @PathVariable Integer itemId, @RequestParam(value = "unit", required = true) ItemContainer.Unit unit) {
        ambulanceService.editItemUnit(licensePlate, itemId, unit);
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

    @PostMapping("/{licensePlate}/crew")
    public void addMedics(@PathVariable String licensePlate, @RequestBody List<Integer> medicIds) {
       ambulanceService.assignMedics(licensePlate, medicIds);
    }

    @DeleteMapping("/{licensePlate}/crew")
    public void removeMedics(@PathVariable String licensePlate, @RequestBody Integer[] medicIds) {
        ambulanceService.removeMedics(licensePlate, List.of(medicIds));
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
        ambulanceService.removeItemById(licensePlate, itemId, count);
    }

    @Operation(summary = "Removes all items of given id from an ambulance")
    @DeleteMapping("/{licensePlate}/items/remove/{itemId}/all")
    public void removeAllItemsOfId(
            @PathVariable String licensePlate,
            @PathVariable Integer itemId) {
        ambulanceService.removeAllItemById(licensePlate, itemId);
    }

    @DeleteMapping("/{licensePlate}/items/remove/all")
    public void removeAllItems(@PathVariable String licensePlate) {
        ambulanceService.clearInventory(licensePlate);
    }
}
