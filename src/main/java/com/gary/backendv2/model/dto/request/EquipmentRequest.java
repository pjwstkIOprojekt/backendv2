package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.EquipmentType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class EquipmentRequest {

    @NotBlank
    EquipmentType equipmentType;

    @NotBlank
    String name;

    @DateTimeFormat
    LocalDate date;
}
