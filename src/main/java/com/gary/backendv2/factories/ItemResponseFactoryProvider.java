package com.gary.backendv2.factories;

import com.gary.backendv2.exception.UnsupportedItemTypeException;
import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.factories.impl.AmbulanceEquipmentFactory;
import com.gary.backendv2.factories.impl.response.AmbulanceEquipmentItemResponseFactory;
import com.gary.backendv2.factories.impl.response.MedicineItemResponseFactory;
import com.gary.backendv2.factories.impl.response.MultiUseItemResponseFactory;
import com.gary.backendv2.factories.impl.response.SingleUseItemResponseFactory;
import com.gary.backendv2.model.enums.ItemType;
import org.apache.commons.lang3.NotImplementedException;

public class ItemResponseFactoryProvider {
    public static ItemResponseAbstractFactory getItemFactory(ItemType itemType) {
        switch (itemType) {
            case SINGLE_USE -> {
                return new SingleUseItemResponseFactory();
            }
            case MEDICAL -> {
                return new MedicineItemResponseFactory();
            }
            case AMBULANCE_EQUIPMENT -> {
                return new AmbulanceEquipmentItemResponseFactory();
            }
            case MULTI_USE -> {
                return new MultiUseItemResponseFactory();
            }
        }

        throw new UnsupportedItemTypeException("Item type: " + itemType + " not supported");
    }
}
