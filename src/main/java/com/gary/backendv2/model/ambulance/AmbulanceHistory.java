package com.gary.backendv2.model.ambulance;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
public class AmbulanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AmbulanceState> ambulanceStates = new LinkedList<>();

}
