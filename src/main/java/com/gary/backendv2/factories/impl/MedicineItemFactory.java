package com.gary.backendv2.factories.impl;

import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.model.dto.request.items.EditItemRequest;
import com.gary.backendv2.model.enums.ItemType;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.MedicineItem;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.dto.request.items.CreateMedicineItemRequest;

public class MedicineItemFactory implements ItemAbstractFactory {
    @Override
    public Item create(AbstractCreateItemRequest itemRequest) {
        CreateMedicineItemRequest medicineItemRequest = (CreateMedicineItemRequest) itemRequest;

        MedicineItem medicineItem = new MedicineItem();
        medicineItem.setName(medicineItemRequest.getName());
        medicineItem.setDescription(medicineItemRequest.getDescription());
        medicineItem.setManufacturer(medicineItemRequest.getManufacturer());
        medicineItem.setExpirationDate(medicineItemRequest.getExpirationDate());

        return medicineItem;
    }
}
