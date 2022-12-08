package com.gary.backendv2.utils;

import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.model.inventory.items.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemUtils {
    private static ItemUtils instance;

    public ItemUtils() {
        itemMeasuringUnitsLookup.put(SingleUseItem.class, ItemContainer.Unit.COUNT);
        itemMeasuringUnitsLookup.put(MedicineItem.class, ItemContainer.Unit.BOX);
        itemMeasuringUnitsLookup.put(AmbulanceEquipmentItem.class, ItemContainer.Unit.COUNT);
        itemMeasuringUnitsLookup.put(MultiUseItem.class, ItemContainer.Unit.COUNT);
    }

    public static ItemUtils getInstance() {
        return Objects.requireNonNullElseGet(instance, ItemUtils::new);
    }

    public Map<Class<? extends Item>, ItemContainer.Unit> itemMeasuringUnitsLookup = new HashMap<>();
}
