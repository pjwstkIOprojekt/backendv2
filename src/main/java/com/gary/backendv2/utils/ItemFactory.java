package com.gary.backendv2.utils;

import com.gary.backendv2.model.ConsumableItem;
import com.gary.backendv2.model.Item;
import com.gary.backendv2.model.SingleUseItem;
import com.gary.backendv2.model.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemFactory {
    public Item createItem(ItemType itemType) {
        switch (itemType) {
            case SINGLE_USE -> {
                return new SingleUseItem();
            }
            case CONSUMABLE -> {
                return new ConsumableItem();
            }
            case MEDICAL_EQUIPMENT -> throw new NotImplementedException(itemType + " not implemented");
        }

        return null;
    }
}
