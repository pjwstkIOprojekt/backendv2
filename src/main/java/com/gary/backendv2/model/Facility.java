package com.gary.backendv2.model;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.dto.response.geocoding.MaptilerResponse;
import com.gary.backendv2.model.enums.FacilityType;
import com.gary.backendv2.service.GeocodingService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facility  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facilityId;

	private String name;

	@Embedded
	private Location location;

	private String address;

	@Enumerated(EnumType.STRING)
	private FacilityType facilityType;

	public Facility create(FacilityRequest facilityRequest, GeocodingService geocodingService) {
		MaptilerResponse geoResponse = geocodingService.getAddressFromCoordinates(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()));

		Facility facility = new Facility();
		facility.setFacilityId(null);
		facility.setFacilityType(facilityRequest.getFacilityType());
		facility.setAddress(geoResponse.getFeatures().size() > 0 ? geoResponse.getFeatures().get(0).getPlaceName() : "UNKNOWN");
		facility.setName(facilityRequest.getName());
		facility.setLocation(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()));

		return facility;
	}

	public void accept(EntityVisitor ev, GeocodingService geocodingService, List<BaseRequest> baseRequest) {
		ev.visit(this, geocodingService, baseRequest);
	}
}
