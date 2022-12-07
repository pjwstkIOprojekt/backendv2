package com.gary.backendv2.model.users.employees;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shiftId;

    private LocalDateTime scheduledStartTime;
    private LocalDateTime scheduledEndTime;

    private long startTimeDelta;

    private LocalDateTime actualStartTime;
    private LocalDateTime expectedEndTime;
    private LocalDateTime actualEndTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AbstractEmployee employee;

}
