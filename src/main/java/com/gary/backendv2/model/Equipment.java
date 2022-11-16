package com.gary.backendv2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gary.backendv2.model.enums.EquipmentType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@Table(name = "equipment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer equipmentId;

    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;

    private String name;

    private LocalDate date;

    private boolean isEquipped;

    @OneToMany(mappedBy = "equipment")
    @JsonIgnore
    private Set<EquipmentInAmbulance> equipmentInAmbulances;

}
