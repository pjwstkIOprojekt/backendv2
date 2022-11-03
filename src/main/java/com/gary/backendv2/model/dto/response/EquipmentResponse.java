package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.EquipmentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EquipmentResponse {
    Integer equipmentId;
    EquipmentType equipmentType;
    String name;
    LocalDate date;
}
