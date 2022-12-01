package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.dto.request.users.UpdateWorkScheduleRequest;
import com.gary.backendv2.model.dto.response.WorkScheduleResponse;
import com.gary.backendv2.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/schedule/update")
    @Operation(summary = "Change logged user's work schedule", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> changeSchedule(@RequestBody UpdateWorkScheduleRequest workSchedule, Authentication authentication) {
        return ResponseEntity.ok(employeeService.updateWorkSchedule(workSchedule, authentication));
    }

}
