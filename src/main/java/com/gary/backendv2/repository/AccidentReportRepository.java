package com.gary.backendv2.repository;

import com.gary.backendv2.model.AccidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccidentReportRepository extends JpaRepository<AccidentReport, Integer> {
	Optional<AccidentReport> findByAccidentId(Integer id);
}
