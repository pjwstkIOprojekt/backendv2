package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.FacilityType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FacilityRequest {
	@NotNull
	private Double longitude;
	@NotNull
	private Double latitude;
	@NotBlank
	private String name;
	@NotNull
	private FacilityType facilityType;
}
