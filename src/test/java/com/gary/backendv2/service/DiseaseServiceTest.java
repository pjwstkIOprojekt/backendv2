package com.gary.backendv2.service;

import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Disease;
import com.gary.backendv2.repository.DiseaseRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
