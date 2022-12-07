package com.gary.backendv2.model.ambulance;

import com.gary.backendv2.model.users.employees.Medic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer crewId;

    @OneToMany
    private Set<Medic> medics = new HashSet<>();
}
