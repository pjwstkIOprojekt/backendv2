package com.gary.backendv2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentInAmbulance {

    @EmbeddedId
    EquipmentInAmbulanceKey id;

    @ManyToOne
    @MapsId("equipmentId")
    @JoinColumn(name = "equipmentId")
    Equipment equipment;

    @ManyToOne
    @MapsId("ambulanceId")
    @JoinColumn(name = "ambulanceId")
    Ambulance ambulance;

    private LocalDateTime date;

    private Double amount;

    private Double usage;

    private Double waste;

    private Double unitsOfMeasure;

    private String comments;

}
