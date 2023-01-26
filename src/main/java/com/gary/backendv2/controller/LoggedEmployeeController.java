package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.dto.request.users.UpdateWorkScheduleRequest;
import com.gary.backendv2.model.dto.response.WorkScheduleResponse;
import com.gary.backendv2.model.users.AdminUser;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class LoggedEmployeeController {
    private final EmployeeService employeeService;
    private final AuthService authService;

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
        if (authService.getLoggedUserFromAuthentication(authentication) instanceof AdminUser) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(employeeService.updateWorkSchedule(workSchedule, authentication));
    }

    @GetMapping("/schedule")
    @Operation(summary = "Change logged user's work schedule", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getSchedule(Authentication authentication) {
        if (authService.getLoggedUserFromAuthentication(authentication) instanceof AdminUser) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(employeeService.getSchedule(authentication));
    }

    @GetMapping("/schedule/is-working")
    @Operation(summary = "Get whether logged employee is working or not", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getShiftStatus(Authentication authentication) {
        return ResponseEntity.ok(employeeService.amIWorking(authentication));
    }

    @GetMapping("/medic/assigned-to")
    @Operation(summary = "Find to which ambulance you are assigned to", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> findAssignedAmbulance(Authentication authentication) {
        return ResponseEntity.ok(employeeService.findAssignedAmbulance(authentication));
    }
}
