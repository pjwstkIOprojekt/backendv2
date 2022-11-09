package com.gary.backendv2.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shiftId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private long startTimeDelta;

    private LocalDateTime actualEndTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AbstractEmployee employee;

}
