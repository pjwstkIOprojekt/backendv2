package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.AmbulanceStateType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class AmbulanceStateResponse {
    private AmbulanceStateType type;
    private LocalDateTime timestamp;
}
