package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.UnsupportedItemTypeException;
import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.factories.ItemFactoryProvider;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.repository.ItemContainerRepository;
import com.gary.backendv2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemContainerRepository itemContainerRepository;

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    public Item getById(Integer itemId) {
        Optional<Item> itemOptional = itemRepository.findByItemId(itemId);

        return itemOptional.orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND));
    }

    public Item createItem(AbstractCreateItemRequest itemRequest) {
        ItemAbstractFactory itemFactory;
        try {
            itemFactory = ItemFactoryProvider.getItemFactory(itemRequest.getType());
        }catch (UnsupportedItemTypeException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        Item item = itemFactory.create(itemRequest);

        return itemRepository.save(item);
    }
}
