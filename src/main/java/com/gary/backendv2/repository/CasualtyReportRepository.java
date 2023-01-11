package com.gary.backendv2.repository;

import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.incident.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CasualtyReportRepository extends JpaRepository<CasualtyReport, Integer> {

    List<CasualtyReport> findByIncident(Incident incident);
}
