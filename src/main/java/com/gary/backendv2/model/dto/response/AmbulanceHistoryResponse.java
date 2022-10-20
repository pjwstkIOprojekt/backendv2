package com.gary.backendv2.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AmbulanceHistoryResponse {
    private String licensePlate;
    private Set<AmbulanceStateResponse> ambulanceHistory;
}
