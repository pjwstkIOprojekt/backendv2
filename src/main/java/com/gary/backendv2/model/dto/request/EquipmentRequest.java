package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.EquipmentType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EquipmentRequest {

    @NotNull
    @Enumerated(EnumType.STRING)
    EquipmentType equipmentType;

    @NotBlank
    String name;

    @DateTimeFormat
    LocalDate date;
}
