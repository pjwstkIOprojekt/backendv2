package com.gary.backendv2.controller;

import com.gary.backendv2.repository.FacilityRepository;
import com.gary.backendv2.service.FacilityService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class FacilityControllerTest {
	private final FacilityRepository facilityRepository = mock(FacilityRepository.class);
	private final FacilityService facilityService = new FacilityService(facilityRepository);
	private final FacilityController facilityController = new FacilityController(facilityService);

	@Test
	void getAll(){
		assertEquals(facilityController.getAll().getBody(), facilityService.getAll());
	}
}
