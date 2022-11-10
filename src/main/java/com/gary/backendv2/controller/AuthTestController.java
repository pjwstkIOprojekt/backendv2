package com.gary.backendv2.controller;

import com.gary.backendv2.model.Dispatcher;
import com.gary.backendv2.model.enums.RoleName;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.repository.RoleRepository;
import com.gary.backendv2.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;


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
