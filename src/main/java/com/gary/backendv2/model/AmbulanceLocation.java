package com.gary.backendv2.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AmbulanceLocation implements Comparable<AmbulanceLocation> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer locationId;

    private LocalDateTime timestamp;

    @Embedded
    private Location location;

    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    private Ambulance ambulance;

    @Override
    public int compareTo(AmbulanceLocation a) {
        if (getTimestamp() == null || a.getTimestamp() == null) {
            return 0;
        }
        return getTimestamp().compareTo(a.getTimestamp());
    }
}
