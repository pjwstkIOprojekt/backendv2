package com.gary.backendv2.repository;

import com.gary.backendv2.model.AmbulanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceHistoryRepository extends JpaRepository<AmbulanceHistory, Integer> {
}
