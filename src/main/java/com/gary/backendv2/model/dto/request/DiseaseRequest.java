package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DiseaseRequest {

	@Email
	String userEmail;

	@NotBlank
	private String diseaseName;

	@NotBlank
	private String description;

	@NotBlank
	private boolean shareWithBand;
}
