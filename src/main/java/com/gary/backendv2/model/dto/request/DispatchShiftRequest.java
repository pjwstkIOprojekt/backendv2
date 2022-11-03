package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class DispatchShiftRequest {
    @NotNull
    private LocalDateTime start;
    @Min(1)
    @Max(12)
    private Integer hours = 8;
}
