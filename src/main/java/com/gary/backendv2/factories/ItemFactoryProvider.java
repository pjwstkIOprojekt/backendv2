package com.gary.backendv2.factories;

import com.gary.backendv2.exception.UnsupportedItemTypeException;
import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.factories.impl.AmbulanceEquipmentFactory;
import com.gary.backendv2.factories.impl.MedicineItemFactory;
import com.gary.backendv2.factories.impl.SingleUseItemFactory;
import com.gary.backendv2.factories.impl.MultiUseItemFactory;
import com.gary.backendv2.model.enums.ItemType;

public class ItemFactoryProvider {
    public static ItemAbstractFactory getItemFactory(ItemType itemType) {
        switch (itemType) {
            case SINGLE_USE -> {
                return new SingleUseItemFactory();
            }
            case MEDICAL -> {
                return new MedicineItemFactory();
            }
            case AMBULANCE_EQUIPMENT -> {
                return new AmbulanceEquipmentFactory();
            }
            case MULTI_USE -> {
                return new MultiUseItemFactory();
            }
        }

        throw new UnsupportedItemTypeException("Item type: " + itemType + " not supported");
    }
}
