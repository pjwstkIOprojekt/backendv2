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
public abstract class AbstractItemResponse {
    protected Integer itemId;
    protected ItemType type;
}
