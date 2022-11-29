package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.IncidentReport;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class IncidentReportResponse {
	private Integer accidentId;
	private LocalDateTime date;
	private Location location;
	private String address;
	private String bandCode;
	private EmergencyType emergencyType;
	private int victimCount;
	private boolean consciousness;
	private boolean breathing;
	private String description;

	public static IncidentReportResponse of(IncidentReport incidentReport) {
		return IncidentReportResponse
				.builder()
				.date(incidentReport.getDate())
				.accidentId(incidentReport.getAccidentId())
				.victimCount(incidentReport.getVictimCount())
				.location(incidentReport.getLocation())
				.emergencyType(incidentReport.getEmergencyType())
				.breathing(incidentReport.isBreathing())
				.consciousness(incidentReport.isConscious())
				.bandCode(incidentReport.getBandCode())
				.description(incidentReport.getDescription())
				.address(incidentReport.getAddress())
				.build();
	}
}
