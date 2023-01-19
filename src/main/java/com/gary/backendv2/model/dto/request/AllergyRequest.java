package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AllergyRequest extends BaseRequest{
	@Email
	String userEmail;

	@NotNull
	@Enumerated(EnumType.STRING)
	private AllergyType allergyType;

	@NotBlank
	private String allergyName;

	@NotBlank
	private String other;
}
