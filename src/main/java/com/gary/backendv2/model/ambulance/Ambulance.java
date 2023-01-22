package com.gary.backendv2.model.ambulance;

import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceType;
import com.gary.backendv2.model.inventory.Inventory;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "ambulance")
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

    @OneToOne
    private Crew crew;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "history_id")
    private AmbulanceHistory ambulanceHistory;

    @ManyToMany
    @JoinTable(name = "ambulances_incidents",
            joinColumns = @JoinColumn(name = "ambulance"),
            inverseJoinColumns = @JoinColumn(name = "incident"))
    private Set<Incident> incidents = new LinkedHashSet<>();

    @OneToOne
    private AmbulanceIncidentHistory incidentHistory;

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

    public Ambulance create(AddAmbulanceRequest addRequest, AmbulanceService ambulanceService) {
        return ambulanceService.createAmbulance(addRequest);
    }

    public void accept(EntityVisitor ev, AmbulanceService ambulanceService, List<BaseRequest> baseRequest) {
        ev.visit(this, ambulanceService, baseRequest);
    }
}

