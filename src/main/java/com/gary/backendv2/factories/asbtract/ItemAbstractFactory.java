package com.gary.backendv2.factories.asbtract;

import com.gary.backendv2.model.dto.request.items.EditItemRequest;
import com.gary.backendv2.model.enums.ItemType;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;

public interface ItemAbstractFactory {
    Item create(AbstractCreateItemRequest itemRequest);
}
