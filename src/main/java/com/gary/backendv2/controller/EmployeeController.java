package com.gary.backendv2.controller;

import com.gary.backendv2.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/all/schedule")
    public ResponseEntity<?> getSchedules() {
        return ResponseEntity.ok(employeeService.getAllSchedules());
    }
}
