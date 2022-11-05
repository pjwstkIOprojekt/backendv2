package com.gary.backendv2.service;

import com.gary.backendv2.model.Facility;
import com.gary.backendv2.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {
	private final FacilityRepository facilityRepository;

	public List<Facility> getAll(){
		return facilityRepository.findAll();
	}
}
