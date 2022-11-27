package com.gary.backendv2.model.dto.request.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateMedicineItemRequest extends AbstractCreateItemRequest {
    private String name;
    private String manufacturer;
    private String description;

}
