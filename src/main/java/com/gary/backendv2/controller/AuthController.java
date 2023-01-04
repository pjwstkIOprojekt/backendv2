package com.gary.backendv2.controller;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.dto.request.users.*;
import com.gary.backendv2.model.dto.response.JwtResponse;
import com.gary.backendv2.model.dto.response.ServerResponse;
import com.gary.backendv2.model.dto.response.users.GenericUserResponse;
import com.gary.backendv2.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    AuthService authService;

    @Resource(name="roleHierarchy")
    private RoleHierarchy roleHierarchy;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwt = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            authService.registerUser(signUpRequest);
        } catch (HttpException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }

        return ResponseEntity.ok(new ServerResponse("User registered successfully!", HttpStatus.OK));
    }

    @PutMapping("/password/change")
    @Operation(summary = "Change password", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        authService.changePassword(authentication, changePasswordRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> generatePasswordResetToken(@Valid @RequestBody PasswordResetTokenRequest passwordResetTokenRequest) {
        authService.sendPasswordResetToken(passwordResetTokenRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(token, resetPasswordRequest.getNewPassword());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/info")
    @Operation(summary = "Get user info", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> printAuthentication(Authentication authentication) {
        return ResponseEntity.ok(authService.getUserInfo(authentication));
    }
}
