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
public class MultiUseItemResponse extends AbstractItemResponse {
    private String name;

    // TODO: what else
}
