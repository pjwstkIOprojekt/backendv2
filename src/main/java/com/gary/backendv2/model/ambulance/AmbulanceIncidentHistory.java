package com.gary.backendv2.model.ambulance;

import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.incident.Incident;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class AmbulanceIncidentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @OneToMany
    private List<AmbulanceIncidentHistoryElement> incidents = new ArrayList<>();
}
