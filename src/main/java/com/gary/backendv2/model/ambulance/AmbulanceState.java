package com.gary.backendv2.model.ambulance;

import com.gary.backendv2.model.enums.AmbulanceStateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@Entity
public class AmbulanceState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stateId;

    @Enumerated(EnumType.STRING)
    private AmbulanceStateType stateType;

    private LocalDateTime timestamp;
}
