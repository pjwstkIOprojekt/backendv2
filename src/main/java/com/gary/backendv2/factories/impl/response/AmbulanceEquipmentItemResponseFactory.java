package com.gary.backendv2.factories.impl.response;

import com.gary.backendv2.factories.ItemResponseFactoryProvider;
import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.dto.response.items.AmbulanceEquipmentItemResponse;
import com.gary.backendv2.model.inventory.items.AmbulanceEquipmentItem;
import com.gary.backendv2.model.inventory.items.Item;

public class AmbulanceEquipmentItemResponseFactory implements ItemResponseAbstractFactory {
    @Override
    public AbstractItemResponse createResponse(Item item) {
        AmbulanceEquipmentItem i = (AmbulanceEquipmentItem) item;

        AmbulanceEquipmentItemResponse response = new AmbulanceEquipmentItemResponse();
        response.setName(i.getName());
        response.setManufacturer(i.getManufacturer());
        response.setDescription(i.getDescription());
        response.setType(i.getDiscriminatorValue());
        response.setItemId(i.getItemId());

        return response;
    }
}
