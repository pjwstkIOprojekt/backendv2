package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.model.incident.Incident;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponse {
	private Integer incidentId;
	private Integer dangerScale;
	private IncidentStatusType incidentStatusType;
	private String reactionJustification;
	private IncidentReportResponse accidentReport;

	public IncidentResponse(Incident incident) {
		incidentId = incident.getIncidentId();
		dangerScale = incident.getDangerScale();
		incidentStatusType = incident.getIncidentStatusType();
		reactionJustification = incident.getReactionJustification();
		accidentReport = IncidentReportResponse.of(incident.getIncidentReport());
	}
}
