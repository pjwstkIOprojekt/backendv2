package com.gary.backendv2.factories.asbtract;

import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.inventory.items.Item;

public interface ItemResponseAbstractFactory {
    AbstractItemResponse createResponse(Item item);
}
