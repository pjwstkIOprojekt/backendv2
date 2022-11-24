package com.gary.backendv2.service;

import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.repository.AccidentReportRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccidentReportServiceTest {
	private final AccidentReportRepository accidentReportRepository = mock(AccidentReportRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final GeocodingService geocodingService = mock(GeocodingService.class);
	private final IncidentService incidentService = mock(IncidentService.class);
	private final AccidentReportService accidentReportService = new AccidentReportService(accidentReportRepository, userRepository, incidentService, geocodingService);

	@Test
	void getAll(){
		List<AccidentReport> expected = List.of(new AccidentReport());
		when(accidentReportRepository.findAll()).thenReturn(expected);
		var result = accidentReportService.getAll();
		assertEquals(expected.size(), result.size());
	}
}
