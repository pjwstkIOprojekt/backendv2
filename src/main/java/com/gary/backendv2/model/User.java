package com.gary.backendv2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gary.backendv2.model.security.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
}
