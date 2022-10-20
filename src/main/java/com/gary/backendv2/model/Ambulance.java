package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Transient
    private AmbulanceState currentState;

    public AmbulanceState findCurrentState() {
        currentState = ambulanceHistory.getAmbulanceStates().get(ambulanceHistory.getAmbulanceStates().size() - 1);
        return currentState;
    }


    @Embeddable
    @Getter
    @Setter
    public static class Location {
        String longitude;
        String latitude;

        protected Location() {}

        private Location(String longitude, String latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public static Location of(String longitude, String latitude) {
            return new Location(longitude, latitude);
        }

        public static Location undefined() {
            return new Location("0", "0");
        }
    }
}


