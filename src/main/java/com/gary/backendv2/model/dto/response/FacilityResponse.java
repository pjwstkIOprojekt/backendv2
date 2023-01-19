package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.enums.FacilityType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FacilityResponse {
	private Integer facilityId;
	private String name;
	private Location location;

	private String address;
	private FacilityType facilityType;
}
