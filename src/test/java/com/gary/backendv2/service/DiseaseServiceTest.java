package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Disease;
import com.gary.backendv2.model.dto.request.DiseaseRequest;
import com.gary.backendv2.model.dto.response.AllergyResponse;
import com.gary.backendv2.model.dto.response.DiseaseResponse;
import com.gary.backendv2.repository.DiseaseRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class DiseaseServiceTest {
    private final DiseaseRepository diseaseRepository = mock(DiseaseRepository.class);
    private final MedicalInfoRepository medicalInfoRepository = mock(MedicalInfoRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final DiseaseService diseaseService = new DiseaseService(diseaseRepository, medicalInfoRepository, userRepository);

    @Test
    void getAll() {
        List<Disease> expected = List.of(new Disease());

        when(diseaseRepository.findAll()).thenReturn(expected);

        var result = diseaseService.getAll();

        assertEquals(expected.size(), result.size());
    }

    @Test()
    void getByIdShouldFind() {
        int id = 1;
        Disease expected = new Disease();
        expected.setDiseaseId(1);
        expected.setDiseaseName("test");

        when(diseaseRepository.findByDiseaseId(id)).thenReturn(Optional.of(expected));

        DiseaseResponse result = diseaseService.getAllById(1);

        assertNotNull(result);
        assertEquals(expected.getDiseaseId(), result.getDiseaseId());
        assertEquals(expected.getDiseaseName(), result.getDiseaseName());

    }

    @Test
    void getByIdShouldNotFind() {
        int id = 120;

        when(diseaseRepository.findByDiseaseId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            diseaseService.getAllById(id);
        });

        String expectedMessage = String.format("Cannot find disease with %s", id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteShouldFail() {
        int id = 2137;
        when(diseaseRepository.findByDiseaseId(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpException.class, () -> {
            diseaseService.getAllById(id);
        });
        String expectedMessage = String.format("Cannot find disease with %s", id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteShouldWork() {
        int id = 2137;
        Disease disease = new Disease();
        when(diseaseRepository.findByDiseaseId(id)).thenReturn(Optional.of(disease));

        diseaseService.removeDisease(id);

        verify(diseaseRepository, times(1)).delete(any(Disease.class));
    }

    @Test
    void updateShouldWork() {
        int id = 2137;
        DiseaseRequest diseaseRequest = new DiseaseRequest();
        diseaseRequest.setDiseaseName("test");
        diseaseRequest.setDescription("test");
        diseaseRequest.setShareWithBand(true);
        Disease disease = new Disease();
        when(diseaseRepository.findByDiseaseId(id)).thenReturn(Optional.of(disease));
        diseaseService.updateDisease(2137, diseaseRequest);
        verify(diseaseRepository, times(1)).save(any(Disease.class));

    }

    @Test
    void updateShouldFail() {
        int id = 2138;
        DiseaseRequest diseaseRequest = new DiseaseRequest();
        diseaseRequest.setDiseaseName("test");
        diseaseRequest.setDescription("test");
        diseaseRequest.setShareWithBand(true);
        Exception exc = assertThrows(HttpException.class, () -> {
            diseaseService.updateDisease(id, diseaseRequest);
        });
    }
}
