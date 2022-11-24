package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.IncidentStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IncidentResponse {
	private Integer incidentId;
	private Integer dangerScale;
	private IncidentStatusType incidentStatusType;
	private String reactionJustification;
	private AccidentReportResponse accidentReport;
}
