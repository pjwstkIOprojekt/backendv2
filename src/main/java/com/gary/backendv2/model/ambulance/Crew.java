package com.gary.backendv2.model.ambulance;

import com.gary.backendv2.model.users.employees.Medic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer crewId;

    @OneToMany
    private List<Medic> medics = new ArrayList<>();
}
