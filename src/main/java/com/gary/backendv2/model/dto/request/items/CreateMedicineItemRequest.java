package com.gary.backendv2.model.dto.request.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateMedicineItemRequest extends AbstractCreateItemRequest {
    private String name;
    private String manufacturer;
    private String description;
    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

}
