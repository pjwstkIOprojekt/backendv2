package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.FacilityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityRequest {
	private Double longitude;
	private Double latitude;
	private FacilityType facilityType;
}
