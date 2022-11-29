package com.gary.backendv2.security.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Dispatcher;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.WorkSchedule;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.model.dto.request.LoginRequest;
import com.gary.backendv2.model.dto.request.SignupRequest;
import com.gary.backendv2.model.dto.response.JwtResponse;
import com.gary.backendv2.model.enums.RoleName;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.RoleRepository;
import com.gary.backendv2.repository.UserRepository;
import com.gary.backendv2.repository.WorkScheduleRepository;
import com.gary.backendv2.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private MedicalInfoRepository medicalInfoRepository;

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

        MedicalInfo mi = new MedicalInfo();
        mi = medicalInfoRepository.save(mi);

        User user = User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .birthDate(signupRequest.getBirthDate())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .userId(null)
                .medicalInfo(mi)
                .build();


        Role userRole = Optional.ofNullable(roleRepository.findByName(RoleName.USER.getPrefixedName())).orElseThrow(() -> new RuntimeException("Role " + RoleName.USER + " not found!"));

        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    public void registerEmployee(EmployeeType employeeType, SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Email already in use!");
        }

        switch (employeeType) {
            case DISPATCHER -> registerDispatcher(signupRequest);
            case PARAMEDIC -> throw new NotImplementedException("Paramedic not yet implemented");
        }
    }

    private  final WorkScheduleRepository workScheduleRepository;
    private void registerDispatcher(SignupRequest signupRequest) {
        String scheduleDefinition = getDefaultWorkSchedule();
        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setSchedule(scheduleDefinition);
        workSchedule.setCreatedAt(LocalDateTime.now());

        workSchedule = workScheduleRepository.save(workSchedule);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setFirstName(signupRequest.getFirstName());
        dispatcher.setLastName(signupRequest.getLastName());
        dispatcher.setEmail(signupRequest.getEmail());
        dispatcher.setPhoneNumber(signupRequest.getPhoneNumber());
        dispatcher.setWorkSchedule(workSchedule);
        dispatcher.setBirthDate(signupRequest.getBirthDate());
        dispatcher.setPassword(passwordEncoder.encode(signupRequest.getPassword()));


        Role userRole = Optional.ofNullable(roleRepository.findByName(RoleName.DISPATCHER.getPrefixedName())).orElseThrow(() -> new RuntimeException("Role " + RoleName.DISPATCHER + " not found!"));

        dispatcher.setRoles(Set.of(userRole));
        userRepository.save(dispatcher);
    }

    private String getDefaultWorkSchedule() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:default-work-schedule.json");

        String json;
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            json = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading default work schedule");
        }

        return json;
    }
    public User getLoggedUserFromAuthentication(Authentication authentication) {
        UserPrincipal loggedPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.getByEmail(loggedPrincipal.getUsername());
    }
}
