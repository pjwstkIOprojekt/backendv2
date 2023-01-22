package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.AmbulanceIncidentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceIncidentHistoryRepository extends JpaRepository<AmbulanceIncidentHistory, Integer> {
}
