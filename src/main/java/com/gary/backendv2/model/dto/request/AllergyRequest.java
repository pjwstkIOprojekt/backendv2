package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AllergyRequest {
	@NotBlank
	Integer medicalInfoId;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private AllergyType allergyType;

	@NotBlank
	private String allergyName;

	@NotBlank
	private String other;
}
