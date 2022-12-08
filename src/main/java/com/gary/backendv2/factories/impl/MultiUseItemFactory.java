package com.gary.backendv2.factories.impl;

import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.dto.request.items.CreateMultiUseItemRequest;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.MultiUseItem;

public class MultiUseItemFactory implements ItemAbstractFactory {
    @Override
    public Item create(AbstractCreateItemRequest itemRequest) {
        CreateMultiUseItemRequest i = (CreateMultiUseItemRequest) itemRequest;

        MultiUseItem multiUseItem = new MultiUseItem();
        multiUseItem.setName(i.getName());

        return multiUseItem;
    }
}
