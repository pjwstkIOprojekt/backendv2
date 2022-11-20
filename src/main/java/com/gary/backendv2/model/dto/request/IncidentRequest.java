package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.IncidentStateType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class IncidentRequest {
	@NotNull
	@Min(1)
	@Max(10)
	private Integer dangerScale;
	@NotNull
	private IncidentStateType incidentStateType;
	@NotBlank
	private String reactionJustification;
}
