package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.AllergyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Builder
public class AllergyResponse {
	private Integer allergyId;
	private AllergyType allergyType;
	private String allergyName;
	private String other;
}
