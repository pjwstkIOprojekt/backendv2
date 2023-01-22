package com.gary.backendv2.controller;

import com.gary.backendv2.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/medic")
    @Operation(summary = "Get all medics", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getMedics() { return ResponseEntity.ok(employeeService.getAllMedics()); }

    @GetMapping("/dispatcher")
    @Operation(summary = "Get all dispatchers", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getDispatchers() {
        return ResponseEntity.ok(employeeService.getAllDispatchers());
    }

    @GetMapping("/ambulance-manager")
    @Operation(summary = "Get all ambulance mangers", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAmbulanceManagers() {
        return ResponseEntity.ok(employeeService.getAllAmbulanceManagers());
    }

    @GetMapping("/medic/free")
    @Operation(summary = "Get all unassigned medics", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getFreeMedics() {
        return ResponseEntity.ok(employeeService.getFreeMedics());
    }

    @GetMapping("/all/schedule")
    @Operation(summary = "Get all work schedules", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getSchedules() {
        return ResponseEntity.ok(employeeService.getAllSchedules());
    }
}
