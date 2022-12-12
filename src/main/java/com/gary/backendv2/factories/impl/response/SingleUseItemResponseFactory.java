package com.gary.backendv2.factories.impl.response;

import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.dto.response.items.SingleUseItemResponse;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.inventory.items.SingleUseItem;

public class SingleUseItemResponseFactory implements ItemResponseAbstractFactory {
    @Override
    public AbstractItemResponse createResponse(Item item) {
        SingleUseItem i = (SingleUseItem) item;

        SingleUseItemResponse itemResponse = new SingleUseItemResponse();
        itemResponse.setItemId(i.getItemId());
        itemResponse.setDescription(i.getDescription());
        itemResponse.setName(i.getName());
        itemResponse.setType(i.getDiscriminatorValue());


        return itemResponse;
    }
}
