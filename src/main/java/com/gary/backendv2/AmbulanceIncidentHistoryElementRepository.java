package com.gary.backendv2;

import com.gary.backendv2.model.ambulance.AmbulanceIncidentHistoryElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceIncidentHistoryElementRepository extends JpaRepository<AmbulanceIncidentHistoryElement, Integer> {
}