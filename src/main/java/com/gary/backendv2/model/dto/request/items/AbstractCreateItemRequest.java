package com.gary.backendv2.model.dto.request.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.enums.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateSingleUseItemRequest.class, name = "SINGLE_USE"),
        @JsonSubTypes.Type(value = CreateMedicineItemRequest.class, name = "MEDICAL"),
        @JsonSubTypes.Type(value = CreateAmbulanceEquipmentItemRequest.class, name = "AMBULANCE_EQUIPMENT"),
        @JsonSubTypes.Type(value = CreateMultiUseItemRequest.class, name = "MULTI_USE")
})
@NoArgsConstructor
public abstract class AbstractCreateItemRequest extends BaseRequest {
    protected ItemType type;
}
