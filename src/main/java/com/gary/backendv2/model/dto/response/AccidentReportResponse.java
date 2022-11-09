package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AccidentReportResponse {
	private Integer accidentId;
	private LocalDate date;
	private Location location;
	private String bandCode;
	private EmergencyType emergencyType;
	private int victimCount;
	private boolean consciousness;
	private boolean breathing;
}
