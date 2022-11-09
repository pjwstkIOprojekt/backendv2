package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccidentReportUpdateRequest {
	private String bandCode;
	@NotNull
	@Enumerated(EnumType.STRING)
	private EmergencyType emergencyType;
	@Min(1)
	@NotNull
	private int victimCount;
	@NotBlank
	private Double longitude;
	@NotBlank
	private Double latitude;
	@NotBlank
	private boolean consciousness;
	@NotBlank
	private boolean breathing;
}
