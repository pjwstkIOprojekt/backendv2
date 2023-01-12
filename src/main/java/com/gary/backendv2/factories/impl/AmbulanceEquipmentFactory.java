package com.gary.backendv2.factories.impl;

import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.dto.request.items.CreateAmbulanceEquipmentItemRequest;
import com.gary.backendv2.model.dto.request.items.EditItemRequest;
import com.gary.backendv2.model.enums.ItemType;
import com.gary.backendv2.model.inventory.items.AmbulanceEquipmentItem;
import com.gary.backendv2.model.inventory.items.Item;

public class AmbulanceEquipmentFactory implements ItemAbstractFactory {
    @Override
    public Item create(AbstractCreateItemRequest itemRequest) {
        CreateAmbulanceEquipmentItemRequest ambulanceItemRequest = (CreateAmbulanceEquipmentItemRequest) itemRequest;

        AmbulanceEquipmentItem ambulanceItem = new AmbulanceEquipmentItem();
        ambulanceItem.setManufacturer(ambulanceItemRequest.getManufacturer());
        ambulanceItem.setName(ambulanceItemRequest.getName());
        ambulanceItem.setDescription(ambulanceItemRequest.getDescription());

        return ambulanceItem;
    }
}
