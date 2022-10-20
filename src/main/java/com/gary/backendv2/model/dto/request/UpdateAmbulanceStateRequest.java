package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.AmbulanceStateType;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class UpdateAmbulanceStateRequest {
    private AmbulanceStateType stateType;
    private LocalDateTime start;
    private LocalDateTime end;
}
