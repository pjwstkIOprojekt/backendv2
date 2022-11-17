package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.ItemCountUnit;
import com.gary.backendv2.model.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AmbulanceInventoryResponse {
    @Getter
    @Setter
    public static class ItemResponse {
        private String name;
        private int amount;
        private ItemCountUnit unit;
    }

    @Getter
    @Setter
    public static class ItemGroupResponse {
        private int itemsInGroup;
        private List<ItemResponse> items = new ArrayList<>();
    }

    private String licensePlate;
    private Map<ItemType, ItemGroupResponse> inventory = new HashMap<>();
}
