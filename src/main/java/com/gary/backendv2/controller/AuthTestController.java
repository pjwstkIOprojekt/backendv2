package com.gary.backendv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/hello")
public class AuthTestController {
    @GetMapping("/admin")
    @Operation(summary = "Endpoint for testing authentication flow", security = @SecurityRequirement(name = "bearerAuth"))
    public String helloAdmin() {
        return "Hello admin ;)";
    }

    @GetMapping("/user")
    @Operation(summary = "Endpoint for testing authentication flow", security = @SecurityRequirement(name = "bearerAuth"))
    public String helloUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(x -> System.out.println(x.getAuthority()));
        return "Hello user";
    }
}
