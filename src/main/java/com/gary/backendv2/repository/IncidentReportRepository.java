package com.gary.backendv2.repository;

import com.gary.backendv2.model.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Integer> {
	Optional<IncidentReport> findByAccidentId(Integer id);

	List<IncidentReport> findAllByAccidentIdIn(List<Integer> ids);
}
