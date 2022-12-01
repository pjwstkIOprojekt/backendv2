package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Integer> {
}