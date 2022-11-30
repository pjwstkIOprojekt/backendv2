package com.gary.backendv2.factories;

import com.gary.backendv2.exception.UnsupportedItemTypeException;
import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.factories.impl.MedicineItemResponseFactory;
import com.gary.backendv2.factories.impl.SingleUseItemResponseFactory;
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
            case MULTI_USE, AMBULANCE_EQUIPMENT -> throw new NotImplementedException();
        }

        throw new UnsupportedItemTypeException("Item type: " + itemType + " not supported");
    }
}
