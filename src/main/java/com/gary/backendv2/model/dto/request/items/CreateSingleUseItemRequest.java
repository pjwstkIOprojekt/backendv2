package com.gary.backendv2.model.dto.request.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateSingleUseItemRequest extends AbstractCreateItemRequest {
    private String name;
    private String description;
}
