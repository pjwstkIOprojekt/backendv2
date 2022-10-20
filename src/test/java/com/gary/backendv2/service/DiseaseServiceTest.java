package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Disease;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiseaseServiceTest {
	private final DiseaseRepository diseaseRepository = mock(DiseaseRepository.class);
	private final MedicalInfoRepository medicalInfoRepository = mock(MedicalInfoRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final DiseaseService diseaseService = new DiseaseService(diseaseRepository, medicalInfoRepository, userRepository);

	@Test
	void getAll(){
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

}
