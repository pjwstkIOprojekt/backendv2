package com.gary.backendv2.factories.impl;

import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.dto.response.items.MedicineItemResponse;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.MedicineItem;

public class MedicineItemResponseFactory implements ItemResponseAbstractFactory {
    @Override
    public AbstractItemResponse createResponse(Item item) {
        MedicineItem i = (MedicineItem) item;

        MedicineItemResponse itemResponse = new MedicineItemResponse();
        itemResponse.setItemId(i.getItemId());
        itemResponse.setDescription(i.getDescription());
        itemResponse.setManufacturer(i.getManufacturer());
        itemResponse.setExpirationDate(i.getExpirationDate());
        itemResponse.setName(i.getName());
        itemResponse.setType(i.getDiscriminatorValue());

        return itemResponse;
    }
}
