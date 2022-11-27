package com.gary.backendv2.repository;

import com.gary.backendv2.model.inventory.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByItemId(Integer itemId);

    List<Item> getItemsByItemIdIn(List<Integer> itemIds);
}