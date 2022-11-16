package com.gary.backendv2.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EquipmentInAmbulanceRequest {

    @NotBlank
    Double amount;
    Double usage;
    Double waste;
    Double unitsOfMeasure;
    String comments;
}
