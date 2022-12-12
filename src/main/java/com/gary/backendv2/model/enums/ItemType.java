package com.gary.backendv2.model.enums;

import com.gary.backendv2.model.inventory.items.AmbulanceEquipmentItem;
import com.gary.backendv2.model.inventory.items.MedicineItem;
import com.gary.backendv2.model.inventory.items.MultiUseItem;
import com.gary.backendv2.model.inventory.items.SingleUseItem;

public enum ItemType {
    SINGLE_USE(SingleUseItem.class),
    MEDICAL(MedicineItem.class),
    MULTI_USE(MultiUseItem.class),
    AMBULANCE_EQUIPMENT(AmbulanceEquipmentItem.class);

    private final Class<?> mappedClass;

    ItemType(Class<?> clazz) {
        this.mappedClass = clazz;
    }

    public Class<?> getMappedClass() {
        return mappedClass;
    }

    public static ItemType fromClass(Class<?> clazz) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.mappedClass.equals(clazz)) {
                return itemType;
            }
        }

        return null;
    }
}
