package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.*;
import com.gary.backendv2.model.dto.request.IncidentRequest;
import com.gary.backendv2.model.dto.response.IncidentResponse;
import com.gary.backendv2.repository.IncidentReportRepository;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.DispatcherRepository;
import com.gary.backendv2.repository.IncidentRepository;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IncidentServiceTest {
    private final IncidentRepository incidentRepository = mock(IncidentRepository.class);
    private final IncidentReportRepository incidentReportRepository = mock(IncidentReportRepository.class);
    private final AmbulanceRepository ambulanceRepository = mock(AmbulanceRepository.class);
    private final AmbulanceService ambulanceService = mock(AmbulanceService.class);
    private final DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
    private final EmployeeService employeeService = mock(EmployeeService.class);
    private final IncidentService incidentService = new IncidentService(incidentRepository, incidentReportRepository, ambulanceRepository, ambulanceService, dispatcherRepository);

    @Test
    void getAll() {
        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setAccidentId(1);
        Incident incident = new Incident();
        incident.setIncidentReport(incidentReport);
        List<Incident> expected = List.of(incident);
        when(incidentRepository.findAll()).thenReturn(expected);
        var result = incidentService.getAll();
        assertEquals(expected.size(), result.size());
    }

    @Test
    void getByIdShouldFind() {
        int id = 1;
        Incident expected = new Incident();
        expected.setIncidentId(1);
        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setAccidentId(1);
        expected.setIncidentReport(incidentReport);

        when(incidentRepository.findByIncidentId(id)).thenReturn(Optional.of(expected));

        IncidentResponse result = incidentService.getById(1);

        assertNotNull(result);
        assertEquals(expected.getIncidentId(), result.getIncidentId());
    }

    @Test
    void getByIdShouldNotFind() {
        int id = 2137;

        when(incidentRepository.findByIncidentId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            incidentService.getById(id);
        });

        String expectedMessage = String.format("Incident with id %s not found", id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteShouldWork() {
        int id = 2137;
        Incident incident = new Incident();
        when(incidentRepository.findByIncidentId(id)).thenReturn(Optional.of(incident));
        incidentService.delete(id);
        verify(incidentRepository, times(1)).delete(any(Incident.class));
    }

    @Test
    void deleteShouldFail() {
        int id = 2137;

        when(incidentRepository.findByIncidentId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            incidentService.delete(id);
        });

        String expectedMessage = String.format("Incident with id %s not found", id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateShouldWork() {
        int id = 2137;
        Incident incident = new Incident();
        IncidentRequest incidentRequest = new IncidentRequest();
        incidentRequest.setDangerScale(3);
        when(incidentRepository.findByIncidentId(id)).thenReturn(Optional.of(incident));
        incidentService.update(id, incidentRequest);
        verify(incidentRepository, times(1)).save(any(Incident.class));
    }

    @Test
    void updateShouldFail() {
        int id = 2137;
        Incident incident = new Incident();
        IncidentRequest incidentRequest = new IncidentRequest();
        incidentRequest.setDangerScale(3);
        Exception exc = assertThrows(HttpException.class, () -> {
            incidentService.update(id, incidentRequest);
        });
    }
}
