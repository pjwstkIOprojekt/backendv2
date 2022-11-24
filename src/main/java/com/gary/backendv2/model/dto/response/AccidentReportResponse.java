package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AccidentReportResponse {
	private Integer accidentId;
	private LocalDateTime date;
	private Location location;
	private String bandCode;
	private EmergencyType emergencyType;
	private int victimCount;
	private boolean consciousness;
	private boolean breathing;
	private String description;

	public static AccidentReportResponse of(AccidentReport accidentReport) {
		return AccidentReportResponse
				.builder()
				.date(accidentReport.getDate())
				.accidentId(accidentReport.getAccidentId())
				.victimCount(accidentReport.getVictimCount())
				.location(accidentReport.getLocation())
				.emergencyType(accidentReport.getEmergencyType())
				.breathing(accidentReport.isBreathing())
				.consciousness(accidentReport.isConscious())
				.bandCode(accidentReport.getBandCode())
				.description(accidentReport.getDescription())
				.build();
	}
}
