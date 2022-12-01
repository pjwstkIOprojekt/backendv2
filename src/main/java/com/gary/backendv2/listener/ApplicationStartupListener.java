package com.gary.backendv2.listener;

import com.gary.backendv2.exception.AdminAccountExistsException;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.SignupRequest;
import com.gary.backendv2.model.enums.RoleName;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    AuthService authService;

    @Value("${gary.app.admin.credentials.email}")
    private String adminEmail;
    @Value("${gary.app.admin.credentials.password}")
    private String adminPassword;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {


        try {
           createRoles();
       } catch (RuntimeException e) {
           log.info(e.getMessage());
       }

        try {
           createAdminAccount();
           createSampleUsers();
       } catch (Exception e) {
           log.info(e.getMessage());
       }
    }

    private void createRoles() throws RuntimeException {
        for (RoleName role : RoleName.values()) {
            createRole(role);
        }
    }

    private void createRole(RoleName name) {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(name.getPrefixedName());

        try {
            Role r = roleRepository.save(role);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void createSampleUsers() {
        SignupRequest s1 = new SignupRequest();
        s1.setEmail("test@test.pl");
        s1.setPassword(passwordEncoder.encode("test123"));
        s1.setBirthDate(LocalDate.of(2000, 1, 1));
        s1.setFirstName("Test");
        s1.setLastName("Testowski");
        s1.setPhoneNumber("123456789");

        SignupRequest s2 = new SignupRequest();
        s2.setEmail("test2@test.pl");
        s2.setPassword(passwordEncoder.encode("test123"));
        s2.setBirthDate(LocalDate.of(1984, 12, 7));
        s2.setFirstName("Robert");
        s2.setLastName("Kubica");
        s2.setPhoneNumber("9876543231");

        SignupRequest s3 = new SignupRequest();
        s3.setEmail("test3@test.pl");
        s3.setPassword(passwordEncoder.encode("test123"));
        s3.setBirthDate(LocalDate.of(1977, 12, 3));
        s3.setFirstName("Adam");
        s3.setLastName("Małysz");
        s3.setPhoneNumber("111222333");

        List<SignupRequest> regular = List.of(s1, s2, s3);
        regular.forEach(x -> authService.registerUser(x));
    }

    private void createAdminAccount() throws AdminAccountExistsException {
        Optional<User> adminUser = Optional.ofNullable(userRepository.getByEmail("admin@gary.com"));
        if (adminUser.isEmpty()) {
            Optional<Role> adminRole = Optional.ofNullable(roleRepository.findByName(RoleName.ADMIN.getPrefixedName()));
            if (adminRole.isEmpty()) {
                log.error("ADMIN ROLE DOESN'T EXISTS");
                SpringApplication.exit(applicationContext, () -> -1);
                return;
            }

            User user = new User();
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setEmail(adminEmail);
            user.setRoles(Set.of(adminRole.get()));

            userRepository.save(user);

            log.info("Admin account created: email: {}, password: {}", adminEmail, adminPassword);
        } else throw new AdminAccountExistsException("Admin account already exists.");
    }
}
