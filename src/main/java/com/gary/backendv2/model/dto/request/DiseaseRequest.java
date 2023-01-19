package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiseaseRequest extends BaseRequest {

	@Email
	String userEmail;

	@NotBlank
	private String diseaseName;

	@NotBlank
	private String description;

	@NotNull
	private Boolean shareWithBand;
}
