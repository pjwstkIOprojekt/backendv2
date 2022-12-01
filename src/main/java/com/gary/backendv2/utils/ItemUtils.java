package com.gary.backendv2.utils;

import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.MedicineItem;
import com.gary.backendv2.model.inventory.items.SingleUseItem;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class ItemUtils {
    private static ItemUtils instance;

    public ItemUtils() {
        itemMeasuringUnitsLookup.put(SingleUseItem.class, ItemContainer.Unit.COUNT);
        itemMeasuringUnitsLookup.put(MedicineItem.class, ItemContainer.Unit.BOX);
    }

    public static ItemUtils getInstance() {
        if (instance == null) {
            return new ItemUtils();
        }

        else return instance;
    }

    public Map<Class<? extends Item>, ItemContainer.Unit> itemMeasuringUnitsLookup = new HashMap<>();
}
