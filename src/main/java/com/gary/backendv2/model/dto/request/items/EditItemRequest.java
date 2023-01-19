package com.gary.backendv2.model.dto.request.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gary.backendv2.model.dto.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EditItemRequest extends BaseRequest {
    private String name;
    private String manufacturer;
    private String description;
    private LocalDate expirationDate;

}
