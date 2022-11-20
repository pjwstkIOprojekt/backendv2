package com.gary.backendv2.repository;

import com.gary.backendv2.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
	Optional<Incident> findByIncidentId(Integer id);
}
