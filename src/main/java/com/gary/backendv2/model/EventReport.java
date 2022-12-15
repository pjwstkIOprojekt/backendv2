package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.EmergencyType;
import com.gary.backendv2.model.users.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventReport {

    @Id
    @Column(name = "eventReportId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;

    private Integer dangerScale;

    @Embedded
    private Location location;

    private EmergencyType emergencyType;

    @ManyToOne
    private User reporter;

    private String description;
}
