package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceType;
import com.gary.backendv2.model.inventory.Inventory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Ambulance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ambulanceId;

    @Enumerated(EnumType.STRING)
    private AmbulanceType ambulanceType;

    @Enumerated(EnumType.STRING)
    private AmbulanceClass ambulanceClass;

    @Column(unique = true)
    private String licensePlate;

    private Integer seats;


    @Embedded
    private Location location ;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "history_id")
    private AmbulanceHistory ambulanceHistory;

    @ManyToMany
    @JoinTable(name = "ambulances_incidents",
            joinColumns = @JoinColumn(name = "ambulance"),
            inverseJoinColumns = @JoinColumn(name = "disease"))
    private Set<Incident> incidents = new LinkedHashSet<>();

    @Transient
    private AmbulanceState currentState;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    public AmbulanceState getCurrentState() {
        return ambulanceHistory.getAmbulanceStates().get(ambulanceHistory.getAmbulanceStates().size() - 1);
    }

    public AmbulanceState findCurrentState() {
        currentState = ambulanceHistory.getAmbulanceStates().get(ambulanceHistory.getAmbulanceStates().size() - 1);
        return currentState;
    }
    
}


