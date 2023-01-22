package com.gary.backendv2.repository;

import com.gary.backendv2.model.users.employees.AmbulanceManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceManagerRepository extends JpaRepository<AmbulanceManager, Integer> {
}
