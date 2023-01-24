package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.Gender;
import com.gary.backendv2.model.enums.VictimStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VictimInfoRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Gender gender;
    @NotNull
    private VictimStatus status;
}
