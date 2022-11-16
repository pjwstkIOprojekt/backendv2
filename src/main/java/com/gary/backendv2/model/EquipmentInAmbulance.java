package com.gary.backendv2.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "equipment_in_ambulance")
public class EquipmentInAmbulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    Ambulance ambulance;

    private LocalDateTime date;

    private Double amount;

    private Double usage;

    private Double waste;

    private Double unitsOfMeasure;

    private String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EquipmentInAmbulance that = (EquipmentInAmbulance) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
