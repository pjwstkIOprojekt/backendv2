package com.gary.backendv2.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class AmbulanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @OneToMany(orphanRemoval = true)
    private Set<AmbulanceState> ambulanceStates = new LinkedHashSet<>();

}
