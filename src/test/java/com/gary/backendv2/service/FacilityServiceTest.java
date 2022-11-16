package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.dto.response.FacilityResponse;
import com.gary.backendv2.repository.FacilityRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FacilityServiceTest {
	private final FacilityRepository facilityRepository = mock(FacilityRepository.class);
	private final FacilityService facilityService = new FacilityService(facilityRepository);

	@Test
	void getAll(){
		List<Facility> expected = List.of(new Facility());

		when(facilityRepository.findAll()).thenReturn(expected);

		var result = facilityService.getAll();

		assertEquals(expected.size(), result.size());
	}

	@Test
	void getByIdShouldFound(){
		int id = 1;
		Facility expected = new Facility();
		expected.setFacilityId(1);

		when(facilityRepository.findByFacilityId(id)).thenReturn(Optional.of(expected));

		FacilityResponse result = facilityService.getById(1);

		assertNotNull(result);
		assertEquals(expected.getFacilityId(), result.getFacilityId());
	}

	@Test
	void getByIdShouldntFind(){
		int id = 2137;

		when(facilityRepository.findByFacilityId(id)).thenReturn(Optional.empty());

		Exception exception = assertThrows(HttpException.class, () -> {
			facilityService.getById(id);
		});

		String expectedMessage = String.format("Cannot find facility with %s", id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void deleteShouldntWork(){
		int id = 2137;
		when(facilityRepository.findByFacilityId(id)).thenReturn(Optional.empty());
		Exception exception = assertThrows(HttpException.class, () -> {
			facilityService.delete(id);
		});

		String expected = String.format("Cannot find facility with %s", id);
		String actual = exception.getMessage();

		verify(facilityRepository, times(0)).delete(any(Facility.class));
		assertTrue(actual.contains(expected));
	}

	@Test
	void deleteShouldRemove(){
		int id = 1;
		Facility expected = new Facility();
		expected.setFacilityId(1);
		when(facilityRepository.findByFacilityId(id)).thenReturn(Optional.of(expected));

		facilityService.delete(id);

		verify(facilityRepository, times(1)).delete(any(Facility.class));
	}
}
