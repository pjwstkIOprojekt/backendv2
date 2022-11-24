package com.gary.backendv2.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class EquipmentInAmbulanceResponse {

    @NotBlank
    Double amount;
    Double usage;
    Double waste;
    Double unitsOfMeasure;
    String comments;
}
