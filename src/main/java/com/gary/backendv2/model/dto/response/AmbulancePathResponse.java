package com.gary.backendv2.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.PathElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmbulancePathResponse {

    public AmbulancePathResponse(List<PathElement> path) {
        this.path = path;
    }

    public AmbulancePathResponse(List<PathElement> path, int incidentId) {
        this.path = path;
        this.incidentId = incidentId;
    }

    List<PathElement> path;
    Integer incidentId;
}