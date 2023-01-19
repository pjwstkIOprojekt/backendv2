package com.gary.backendv2.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.users.SignupRequest;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.Review;
import com.gary.backendv2.model.TrustedPerson;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer userId;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;

    private String bandCode;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    private MedicalInfo medicalInfo;

    @OneToOne(mappedBy = "user")
    private TrustedPerson trustedPerson;


    @OneToMany(mappedBy = "reviewer")
    @JsonIgnore
    private Set<Review> reviewSet;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "reporter")
    private Set<IncidentReport> incidentReports;

    public Optional<User> create(SignupRequest addRequest, AuthService authService) {
        authService.registerUser(addRequest);

        return Optional.empty();
    }

    public void accept(EntityVisitor ev, AuthService authService, List<BaseRequest> baseRequest) {
        ev.visit(this, authService, baseRequest);
    }
}
