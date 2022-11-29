package com.gary.backendv2.service;

import com.gary.backendv2.model.IncidentReport;
import com.gary.backendv2.repository.IncidentReportRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncidentReportServiceTest {
	private final IncidentReportRepository incidentReportRepository = mock(IncidentReportRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final GeocodingService geocodingService = mock(GeocodingService.class);
	private final IncidentService incidentService = mock(IncidentService.class);
	private final IncidentReportService incidentReportService = new IncidentReportService(incidentReportRepository, userRepository, incidentService, geocodingService);

	@Test
	void getAll(){
		List<IncidentReport> expected = List.of(new IncidentReport());
		when(incidentReportRepository.findAll()).thenReturn(expected);
		var result = incidentReportService.getAll();
		assertEquals(expected.size(), result.size());
	}
}
