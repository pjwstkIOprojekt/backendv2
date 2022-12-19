package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Disease;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.ambulance.AmbulanceHistory;
import com.gary.backendv2.model.ambulance.AmbulanceState;
import com.gary.backendv2.model.dto.request.*;
import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.AmbulanceType;
import com.gary.backendv2.model.enums.EmergencyType;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.dto.response.IncidentReportResponse;
import com.gary.backendv2.repository.IncidentReportRepository;
import com.gary.backendv2.repository.UserRepository;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class IncidentReportServiceTest {
    private final IncidentReportRepository incidentReportRepository = mock(IncidentReportRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final GeocodingService geocodingService = mock(GeocodingService.class);
    private final IncidentService incidentService = mock(IncidentService.class);
    private final IncidentReportService incidentReportService = new IncidentReportService(incidentReportRepository, userRepository, incidentService, geocodingService);

    @Test
    void getAll() {
        List<IncidentReport> expected = List.of(new IncidentReport());
        when(incidentReportRepository.findAll()).thenReturn(expected);
        var result = incidentReportService.getAll();
        assertEquals(expected.size(), result.size());
    }

    @Test
    void getByIdWorks() {
        int id = 2137;
        IncidentReport expected = new IncidentReport();
        expected.setAccidentId(id);

        when(incidentReportRepository.findByAccidentId(id)).thenReturn(Optional.of(expected));

        IncidentReportResponse result = incidentReportService.getById(id);

        assertNotNull(result);
        assertEquals(expected.getAccidentId(), result.getAccidentId());
    }

    @Test
    void getByIdShouldntWork() {
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
    void addShouldWork() {
        IncidentReportRequest incidentReportRequest = new IncidentReportRequest();
        IncidentReport expected = new IncidentReport();
        incidentReportRequest.setBandCode("a");
        incidentReportRequest.setBreathing(true);
        incidentReportRequest.setConcious(true);
        incidentReportRequest.setDescription("A");
        incidentReportRequest.setLatitude(1.1);
        incidentReportRequest.setLongitude(1.2);
        incidentReportRequest.setEmail("a@a.pl");
        incidentReportRequest.setEmergencyType(EmergencyType.COVID);
        incidentReportRequest.setVictimCount(1);

        when(incidentReportRepository.save(any(IncidentReport.class))).thenReturn(expected);
        incidentReportService.add(incidentReportRequest);
        verify(incidentReportRepository, times(1)).save(any(IncidentReport.class));


    }

    @Test
    void updateShouldWork() {
        AccidentReportUpdateRequest accidentReportUpdateRequest = new AccidentReportUpdateRequest();
        IncidentReportRequest incidentReportRequest = new IncidentReportRequest();
        IncidentReport expected = new IncidentReport();
        accidentReportUpdateRequest.setBandCode("a");
        accidentReportUpdateRequest.setBreathing(true);

        accidentReportUpdateRequest.setDescription("A");
        accidentReportUpdateRequest.setLatitude(1.1);
        accidentReportUpdateRequest.setLongitude(1.2);

        accidentReportUpdateRequest.setEmergencyType(EmergencyType.COVID);
        accidentReportUpdateRequest.setVictimCount(1);

        when(incidentReportRepository.findByAccidentId(2137)).thenReturn(Optional.of(expected));
        incidentReportService.updateById(2137, accidentReportUpdateRequest);
        verify(incidentReportRepository, times(1)).save(any(IncidentReport.class));

    }

    @Test
    void updateShouldNotWork() {
        AccidentReportUpdateRequest accidentReportUpdateRequest = new AccidentReportUpdateRequest();
        IncidentReportRequest incidentReportRequest = new IncidentReportRequest();
        IncidentReport expected = new IncidentReport();
        accidentReportUpdateRequest.setBandCode("a");
        accidentReportUpdateRequest.setBreathing(true);

        accidentReportUpdateRequest.setDescription("A");
        accidentReportUpdateRequest.setLatitude(1.1);
        accidentReportUpdateRequest.setLongitude(1.2);

        accidentReportUpdateRequest.setEmergencyType(EmergencyType.COVID);
        accidentReportUpdateRequest.setVictimCount(1);


        Exception exc = assertThrows(HttpException.class, () -> {
            incidentReportService.updateById(2137, accidentReportUpdateRequest);
        });
    }

    @Test
    void deleteShouldFail() {
        int id = 2137;
        when(incidentReportRepository.findByAccidentId(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpException.class, () -> {
            incidentReportService.deleteById(id);
        });
        String expectedMessage = String.format("Cannot find disease with %s", id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteShouldWork() {
        int id = 2137;
        IncidentReport incidentReport = new IncidentReport();
        when(incidentReportRepository.findByAccidentId(id)).thenReturn(Optional.of(incidentReport));

        incidentReportService.deleteById(id);

        verify(incidentReportRepository, times(1)).delete(any(IncidentReport.class));

    }
}
