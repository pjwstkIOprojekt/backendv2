package com.gary.backendv2.controller;

import com.gary.backendv2.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class LoggedEmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/shift/start")
    @Operation(summary = "Start shift for currently logged employee", security = @SecurityRequirement(name = "bearerAuth"))
    public void startShift(Authentication authentication) {
       employeeService.startShift(authentication);


    }

    @GetMapping("/shift/end")
    @Operation(summary = "End shift for currently logged employee", security = @SecurityRequirement(name = "bearerAuth"))
    public void endShift(Authentication authentication) {
        employeeService.endShift(authentication);
    }


}
