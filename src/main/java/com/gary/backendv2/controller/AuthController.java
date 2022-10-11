package com.gary.backendv2.controller;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.dto.request.LoginRequest;
import com.gary.backendv2.model.dto.request.SignupRequest;
import com.gary.backendv2.model.dto.response.JwtResponse;
import com.gary.backendv2.model.dto.response.ServerResponse;
import com.gary.backendv2.security.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    AuthService authService;

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
}
