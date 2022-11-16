package com.gary.backendv2.service;

import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.Incident;
import com.gary.backendv2.model.enums.IncidentStateType;
import com.gary.backendv2.repository.AccidentReportRepository;
import com.gary.backendv2.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidentService {
	private final IncidentRepository incidentRepository;
	private final AccidentReportRepository accidentReportRepository;

	public void addFromReport(AccidentReport accidentReport){
		Incident incident = Incident
				.builder()
				.accidentReport(accidentReport)
				.incidentStateType(IncidentStateType.NEW)
				.build();
		incidentRepository.save(incident);
		accidentReport.setIncident(incident);
		accidentReportRepository.save(accidentReport);
	}
}
