package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.response.items.ItemResponse;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.inventory.items.MedicineItem;
import com.gary.backendv2.model.inventory.items.SingleUseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemResponse> getAll() {
        List<Item> items = itemService.getAll();

        List<ItemResponse> responseList = new ArrayList<>();
        for (Item item : items) {
            ItemResponse itemResponse = createItemResponseObject(item);

            responseList.add(itemResponse);
        }

        return responseList;
    }

    private ItemResponse createItemResponseObject(Item item) {
        ItemResponse itemResponse = new ItemResponse();

        // TODO: THIS IS UGLY, CONVERT TO RESPONSE FACTORY
        switch (item.getDiscriminatorValue()) {
            case SINGLE_USE -> {
                SingleUseItem s = (SingleUseItem) item;

                itemResponse.setItemId(s.getItemId());
                itemResponse.setName(s.getName());
                itemResponse.setDescription(s.getDescription());
                itemResponse.setType(s.getDiscriminatorValue());
            }

            case MEDICINES -> {
                MedicineItem m = (MedicineItem) item;

                itemResponse.setItemId(m.getItemId());
                itemResponse.setManufacturer(m.getManufacturer());
                itemResponse.setType(m.getDiscriminatorValue());
                itemResponse.setDescription(m.getDescription());
                itemResponse.setName(m.getName());
            }
        }
        return itemResponse;
    }

    @PostMapping("/create")
    public ItemResponse createItem(@RequestBody AbstractCreateItemRequest itemRequest) {
        Item item = itemService.createItem(itemRequest);

        return createItemResponseObject(item);
    }
}
