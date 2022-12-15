package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Incident;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.dto.response.IncidentReportResponse;
import com.gary.backendv2.repository.IncidentReportRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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

	@Test
	void getByIdWorks(){
		int id = 2137;
		IncidentReport expected = new IncidentReport();
		expected.setAccidentId(id);

		when(incidentReportRepository.findByAccidentId(id)).thenReturn(Optional.of(expected));

		IncidentReportResponse result = incidentReportService.getById(id);

		assertNotNull(result);
		assertEquals(expected.getAccidentId(), result.getAccidentId());
	}

	@Test
	void getByIdShouldntWork(){
		int id = 2137;

		when(incidentReportRepository.findByAccidentId(id)).thenReturn(Optional.empty());

		Exception exception = assertThrows(HttpException.class, () -> {
			incidentReportService.getById(id);
		});

		String expectedMessage = String.format("Incident report with id %s not found", id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	@Test
	void removeIncidentRaport(){

		int id = 2137;
		IncidentReport incidentReport = new IncidentReport();
		when(incidentReportRepository.findByAccidentId(id)).thenReturn(Optional.of(incidentReport));
		incidentReportService.deleteById(id);
		verify(incidentReportRepository,times(1)).delete(any(IncidentReport.class));

	}
}
