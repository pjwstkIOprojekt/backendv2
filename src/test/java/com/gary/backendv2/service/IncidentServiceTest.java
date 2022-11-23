package com.gary.backendv2.service;

import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.Incident;
import com.gary.backendv2.repository.AccidentReportRepository;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.IncidentRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncidentServiceTest {
	private final IncidentRepository incidentRepository = mock(IncidentRepository.class);
	private final AccidentReportRepository accidentReportRepository = mock(AccidentReportRepository.class);
	private final AccidentReportService accidentReportService = mock(AccidentReportService.class);
	private final AmbulanceRepository ambulanceRepository = mock(AmbulanceRepository.class);
	private final AmbulanceService ambulanceService = mock(AmbulanceService.class);
	private final IncidentService incidentService = new IncidentService(incidentRepository, accidentReportRepository,accidentReportService,ambulanceRepository,ambulanceService);

	@Test
	void getAll(){
		AccidentReport accidentReport = new AccidentReport();
		accidentReport.setAccidentId(1);
		Incident incident = new Incident();
		incident.setAccidentReport(accidentReport);
		List<Incident> expected = List.of(incident);
		when(incidentRepository.findAll()).thenReturn(expected);
		var result = incidentService.getAll();
		assertEquals(expected.size(), result.size());
	}
}
