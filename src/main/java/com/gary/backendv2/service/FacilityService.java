package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.dto.response.FacilityResponse;
import com.gary.backendv2.model.dto.response.geocoding.MaptilerResponse;
import com.gary.backendv2.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacilityService {
	private final FacilityRepository facilityRepository;

	private final GeocodingService geocodingService;

	public List<FacilityResponse> getAll() {
		List<FacilityResponse> facilities = new ArrayList<>();
		for (Facility facility:facilityRepository.findAll()) {
			facilities.add(FacilityResponse
					.builder()
					.facilityId(facility.getFacilityId())
					.name(facility.getName())
					.facilityType(facility.getFacilityType())
					.location(facility.getLocation())
					.build());
		}
		return facilities;
	}

	public FacilityResponse getById(Integer id){
		Optional<Facility> facilityOptional = facilityRepository.findByFacilityId(id);
		if(facilityOptional.isEmpty()){
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find facility with %s", id));
		}
		Facility facility = facilityOptional.get();
		return FacilityResponse
				.builder()
				.facilityId(facility.getFacilityId())
				.name(facility.getName())
				.facilityType(facility.getFacilityType())
				.location(facility.getLocation())
				.build();
	}

	public void add(FacilityRequest facilityRequest) {
		MaptilerResponse geoResponse = geocodingService.getAddressFromCoordinates(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()));

		facilityRepository.save(
				Facility
						.builder()
						.facilityType(facilityRequest.getFacilityType())
						.name(facilityRequest.getName())
						.address(geoResponse.getFeatures().size() > 0 ? geoResponse.getFeatures().get(0).getPlaceName() : "UNKNOWN")
						.location(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()))
						.build()
		);
	}

	public void update(Integer id, FacilityRequest facilityRequest){
		Optional<Facility> facilityOptional = facilityRepository.findByFacilityId(id);
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
		Optional<Facility> facilityOptional = facilityRepository.findByFacilityId(id);
		if(facilityOptional.isEmpty()){
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find facility with %s", id));
		}
		facilityRepository.delete(facilityOptional.get());
	}
}
