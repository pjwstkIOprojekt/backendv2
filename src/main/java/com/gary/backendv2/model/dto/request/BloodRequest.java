package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BloodRequest {

	Integer medicalInfoId;

	Integer userId;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private RhType rhType;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private BloodType bloodType;
}
