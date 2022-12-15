package com.gary.backendv2.factories.impl.response;

import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.dto.response.items.MultiUseItemResponse;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.MultiUseItem;

public class MultiUseItemResponseFactory implements ItemResponseAbstractFactory {
    @Override
    public AbstractItemResponse createResponse(Item item) {
        MultiUseItem i = (MultiUseItem) item;

        MultiUseItemResponse itemResponse = new MultiUseItemResponse();
        itemResponse.setItemId(i.getItemId());
        itemResponse.setType(i.getDiscriminatorValue());
        itemResponse.setName(i.getName());

        return itemResponse;
    }
}
