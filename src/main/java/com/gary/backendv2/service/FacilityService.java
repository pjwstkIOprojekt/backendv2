package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacilityService {
	private final FacilityRepository facilityRepository;

	public List<Facility> getAll(){
		return facilityRepository.findAll();
	}

	public Facility getById(Integer id){
		Optional<Facility> facilityOptional = facilityRepository.getByFacilityId(id);
		if(facilityOptional.isEmpty()){
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find facility with %s", id));
		}
		return facilityOptional.get();
	}

	public void add(FacilityRequest facilityRequest){
		facilityRepository.save(
				Facility
						.builder()
						.facilityType(facilityRequest.getFacilityType())
						.name(facilityRequest.getName())
						.location(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()))
						.build()
		);
	}

	public void update(Integer id, FacilityRequest facilityRequest){
		Optional<Facility> facilityOptional = facilityRepository.getByFacilityId(id);
		if(facilityOptional.isEmpty()){
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find facility with %s", id));
		}
		Facility facility = facilityOptional.get();
		facility.setFacilityType(facilityRequest.getFacilityType());
		facility.setLocation(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()));
		facility.setName(facilityRequest.getName());
		facilityRepository.save(facility);
	}

	public void delete(Integer id){
		Optional<Facility> facilityOptional = facilityRepository.getByFacilityId(id);
		if(facilityOptional.isEmpty()){
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find facility with %s", id));
		}
		facilityRepository.delete(facilityOptional.get());
	}
}
