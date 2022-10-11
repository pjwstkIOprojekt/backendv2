package com.gary.backendv2.security.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.model.dto.request.LoginRequest;
import com.gary.backendv2.model.dto.request.SignupRequest;
import com.gary.backendv2.model.dto.response.JwtResponse;
import com.gary.backendv2.model.enums.RoleName;
import com.gary.backendv2.repository.RoleRepository;
import com.gary.backendv2.repository.UserRepository;
import com.gary.backendv2.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwt(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Set<String> roles = userPrincipal
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return JwtResponse
                .builder()
                .email(userPrincipal.getUsername())
                .token(jwt)
                .roles(roles)
                .type("Bearer ")
                .build();
    }

    public void registerUser(SignupRequest signupRequest) throws HttpException {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Email already in use!");
        }

        User user = User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .birthDate(signupRequest.getBirthDate())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .userId(null)
                .build();


        Role userRole = Optional.ofNullable(roleRepository.findByName(RoleName.USER)).orElseThrow(() -> new RuntimeException("Role " + RoleName.USER + " not found!"));

        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }
}
