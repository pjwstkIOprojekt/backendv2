package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.AmbulanceStateType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAmbulanceStateRequest extends BaseRequest {
    private AmbulanceStateType stateType;
    private LocalDateTime start;
    private LocalDateTime end;
}
