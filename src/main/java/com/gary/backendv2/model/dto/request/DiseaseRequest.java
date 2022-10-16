package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DiseaseRequest {

	Integer medicalInfoId;

	Integer userId;

	@NotBlank
	private String diseaseName;

	@NotBlank
	private String description;

	@NotBlank
	private boolean shareWithBand;
}
