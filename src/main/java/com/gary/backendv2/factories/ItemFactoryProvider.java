package com.gary.backendv2.factories;

import com.gary.backendv2.exception.UnsupportedItemTypeException;
import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.factories.impl.MedicineItemFactory;
import com.gary.backendv2.factories.impl.SingleUseItemFactory;
import com.gary.backendv2.model.enums.ItemType;
import org.apache.commons.lang3.NotImplementedException;

public class ItemFactoryProvider {
    public static ItemAbstractFactory getItemFactory(ItemType itemType) {
        switch (itemType) {
            case SINGLE_USE -> {
                return new SingleUseItemFactory();
            }
            case MEDICAL -> {
                return new MedicineItemFactory();
            }
            case AMBULANCE_EQUIPMENT, MULTI_USE -> throw new NotImplementedException();
        }

        throw new UnsupportedItemTypeException("Item type: " + itemType + " not supported");
    }
}
