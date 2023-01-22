package com.gary.backendv2.model.ambulance;

import com.gary.backendv2.model.incident.Incident;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AmbulanceIncidentHistoryElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyElementId;

    @OneToOne
    private Incident incident;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
