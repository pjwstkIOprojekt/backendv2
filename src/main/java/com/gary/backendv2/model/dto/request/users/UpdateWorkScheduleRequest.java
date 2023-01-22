package com.gary.backendv2.model.dto.request.users;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.utils.annotations.ContainsKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkScheduleRequest extends BaseRequest {
    @ContainsKeys(
            allowedKeys = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"},
            message = "Work schedule keys must have keys from this set {allowedKeys}"
    )
    Map<String, RegisterEmployeeRequest.ScheduleDto> workSchedule = new LinkedHashMap<>();
}
