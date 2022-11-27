package com.gary.backendv2.model.dto.response.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gary.backendv2.model.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// TODO: CONVERT TO ABSTRACT AND EACH ITEM RESPONSE TO INHERIT FROM IT, AND CREATE FACTORY METHODS TO CREATE CORRECT RESPONSE CLASS DEPENDING ON ITEM SOURCE CLASS
// TODO: GROUP RESPONSES BY ITEM TYPE
public class ItemResponse {
    private Integer itemId;
    private ItemType type;
    private String name;
    private String manufacturer;
    private String description;
}
