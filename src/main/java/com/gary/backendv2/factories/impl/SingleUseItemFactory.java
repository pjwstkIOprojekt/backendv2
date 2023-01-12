package com.gary.backendv2.factories.impl;

import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.model.dto.request.items.EditItemRequest;
import com.gary.backendv2.model.enums.ItemType;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.SingleUseItem;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.dto.request.items.CreateSingleUseItemRequest;

public class SingleUseItemFactory implements ItemAbstractFactory {
    @Override
    public Item create(AbstractCreateItemRequest itemRequest) {
        CreateSingleUseItemRequest singleUseItemRequest = (CreateSingleUseItemRequest)  itemRequest;

        SingleUseItem singleUseItem = new SingleUseItem();
        singleUseItem.setName(singleUseItemRequest.getName());
        singleUseItem.setDescription(singleUseItemRequest.getDescription());

        return singleUseItem;
    }
}
