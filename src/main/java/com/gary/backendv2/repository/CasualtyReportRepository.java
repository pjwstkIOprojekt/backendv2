package com.gary.backendv2.repository;

import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.incident.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasualtyReportRepository extends JpaRepository<CasualtyReport, Integer> {

    List<CasualtyReport> findByIncidentAndAmbulanceLicensePlate(Incident incident, String licensePlate);

    List<CasualtyReport> findByIncident(Incident incident);
}
