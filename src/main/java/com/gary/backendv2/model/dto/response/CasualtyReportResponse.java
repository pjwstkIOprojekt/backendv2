package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.incident.Incident;
import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
public class CasualtyReportResponse {

    String description;
    Incident incident;
    Set<Facility> facilities;
    Map<Integer, Integer> itemCounts;
}
