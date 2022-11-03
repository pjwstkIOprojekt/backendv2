package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.EquipmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table(name = "equipment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer equipmentId;

    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;

    private String name;

    private LocalDate date;

    @OneToMany(mappedBy = "equipment")
    private Set<EquipmentInAmbulance> equipmentInAmbulances;

}
