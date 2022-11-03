package com.gary.backendv2.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrustedPersonResponse {
	private Integer trustedId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
}
