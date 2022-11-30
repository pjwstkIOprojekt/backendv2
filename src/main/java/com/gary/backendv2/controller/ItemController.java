package com.gary.backendv2.controller;

import com.gary.backendv2.factories.ItemResponseFactoryProvider;
import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<AbstractItemResponse> getAll() {
       return itemService.getAll().stream().map(x -> {
           ItemResponseAbstractFactory responseFactory = ItemResponseFactoryProvider.getItemFactory(x.getDiscriminatorValue());

           return responseFactory.createResponse(x);
       }).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public AbstractItemResponse getById(@PathVariable Integer itemId) {
        return Stream.of(itemService.getById(itemId)).map(x -> {
            ItemResponseAbstractFactory responseFactory = ItemResponseFactoryProvider.getItemFactory(x.getDiscriminatorValue());

            return responseFactory.createResponse(x);
        }).findAny().get();
    }

    @PostMapping("/create")
    public AbstractItemResponse createItem(@RequestBody AbstractCreateItemRequest itemRequest) {
        Item item = itemService.createItem(itemRequest);

        ItemResponseAbstractFactory responseFactory = ItemResponseFactoryProvider.getItemFactory(item.getDiscriminatorValue());

        return responseFactory.createResponse(item);
    }
}
