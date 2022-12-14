package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.dto.request.users.SignupRequest;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;

    @PostMapping("/register/employee/{employeeType}")
    @Operation(summary = "Register new employee of given type", security = @SecurityRequirement(name = "bearerAuth"))
    public void registerNewEmployee(@Valid @RequestBody RegisterEmployeeRequest newEmployee, @PathVariable EmployeeType employeeType) {
        authService.registerEmployee(employeeType, newEmployee);
    }

}
