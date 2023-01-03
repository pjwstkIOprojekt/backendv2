package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventReportRequest {

    LocalDateTime date;
    @NotBlank
    @Min(value = 1)
    @Max(value = 10)
    Integer dangerScale;
    Location location;
    EmergencyType emergencyType;
    @Email
    String email;
    String description;
}
