package com.gary.backendv2.model.enums;

import com.gary.backendv2.model.inventory.items.MedicineItem;
import com.gary.backendv2.model.inventory.items.SingleUseItem;

public enum ItemType {
    SINGLE_USE(SingleUseItem.class),
    MEDICAL(MedicineItem.class),
    MULTI_USE(Object.class), // NOT IMPLEMENTED YET
    AMBULANCE_EQUIPMENT(Object.class); // NOT IMPLEMENTED YEY

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
