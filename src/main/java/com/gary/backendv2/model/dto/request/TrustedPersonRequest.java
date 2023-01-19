package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Getter
@Setter
public class TrustedPersonRequest extends BaseRequest {
	@Email
	@NotBlank
	private String userEmail;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@Email
	@Nullable
	private String email;

	@NotBlank
	private String phone;
}
