package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.enums.EmergencyType;
import com.gary.backendv2.model.users.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventReportRequest {

    LocalDateTime date;
    Integer dangerScale;
    Location location;
    EmergencyType emergencyType;
    User reporter;
    String description;
}
