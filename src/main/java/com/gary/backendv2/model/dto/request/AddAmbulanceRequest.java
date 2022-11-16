package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class AddAmbulanceRequest {
    @NotNull
    private AmbulanceClass ambulanceClass;
    @NotNull
    private AmbulanceType ambulanceType;
    @Size(min = 8, max = 8)
    private String licensePlate;
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer seats;

    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
}
