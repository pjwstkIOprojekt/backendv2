package com.gary.backendv2.model.dto.response.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceEquipmentItemResponse extends AbstractItemResponse {
    private String name;
    private String manufacturer;
    private String description;
}
