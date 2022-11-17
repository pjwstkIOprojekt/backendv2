package com.gary.backendv2.repository;

import com.gary.backendv2.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}