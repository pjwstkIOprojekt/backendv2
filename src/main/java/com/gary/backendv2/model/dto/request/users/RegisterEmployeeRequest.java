package com.gary.backendv2.model.dto.request.users;

import com.gary.backendv2.utils.annotations.ContainsKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEmployeeRequest extends SignupRequest {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleDto {
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
        String start;

        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
        String end;
    }

    @ContainsKeys(
            allowedKeys = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"},
            message = "Work schedule keys must have keys from this set {allowedKeys}"
    )
    Map<String, ScheduleDto> workSchedule = new LinkedHashMap<>();
}
