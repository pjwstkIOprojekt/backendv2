package com.gary.backendv2.model.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.response.users.UserResponse;
import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class EventReportResponse {
    Integer id;
    LocalDateTime date;
    Integer dangerScale;
    Location location;
    EmergencyType emergencyType;
    @JsonUnwrapped
    UserResponse userResponse;
    String description;
}