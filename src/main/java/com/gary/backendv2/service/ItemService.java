package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.UnsupportedItemTypeException;
import com.gary.backendv2.factories.asbtract.ItemAbstractFactory;
import com.gary.backendv2.factories.ItemFactoryProvider;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.dto.request.items.EditItemRequest;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.model.inventory.items.*;
import com.gary.backendv2.repository.ItemContainerRepository;
import com.gary.backendv2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.channels.MulticastChannel;
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

    public void delete(Integer itemId) {
        try {
            itemRepository.deleteById(itemId);
        } catch (EmptyResultDataAccessException e) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Item %s not found", itemId));
        }
    }

    public Item edit(Integer itemId, EditItemRequest itemRequest) {
        Item item = itemRepository.getReferenceById(itemId);

        Item refHolder;

        switch (item.getDiscriminatorValue()) {
            case AMBULANCE_EQUIPMENT -> {
                AmbulanceEquipmentItem i = (AmbulanceEquipmentItem) item;

                i.setName(itemRequest.getName());
                i.setDescription(itemRequest.getDescription());
                i.setManufacturer(itemRequest.getManufacturer());

                refHolder = i;
            }

            case SINGLE_USE -> {
                SingleUseItem i = (SingleUseItem) item;

                i.setDescription(itemRequest.getDescription());
                i.setName(itemRequest.getName());

                refHolder = i;
            }

            case MULTI_USE -> {
                MultiUseItem i = (MultiUseItem) item;

                i.setName(itemRequest.getName());

                refHolder = i;
            }

            case MEDICAL -> {
                MedicineItem i = (MedicineItem) item;

                i.setName(itemRequest.getName());
                i.setDescription(itemRequest.getDescription());
                i.setManufacturer(itemRequest.getManufacturer());
                i.setExpirationDate(itemRequest.getExpirationDate());

                refHolder = i;
            }

            default -> throw new NotImplementedException();
        }

        return itemRepository.save(refHolder);
    }
}
