package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.AmbulanceState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceStateRepository extends JpaRepository<AmbulanceState, Integer> {
}
