package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class IncidentReportRequest extends BaseRequest {
	@Email
	private String email;
	private String bandCode;
	@NotNull
	@Enumerated(EnumType.STRING)
	private EmergencyType emergencyType;
	@Min(1)
	@NotNull
	private Integer victimCount;
	@NotNull
	private Double longitude;
	@NotNull
	private Double latitude;
	@NotNull
	private Boolean concious;
	@NotNull
	private Boolean breathing;
	private String description;
}
