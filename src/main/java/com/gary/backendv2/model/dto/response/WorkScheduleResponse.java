package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkScheduleResponse {
    Map<String, RegisterEmployeeRequest.ScheduleDto> newSchedule = new HashMap<>();
}
