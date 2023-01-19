package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostAmbulanceLocationRequest extends BaseRequest{
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
}
