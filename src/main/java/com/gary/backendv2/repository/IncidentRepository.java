package com.gary.backendv2.repository;

import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.enums.IncidentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
	Optional<Incident> findByIncidentId(Integer id);
	List<Incident> findAllByIncidentStatusType(IncidentStatusType statusType);
}
