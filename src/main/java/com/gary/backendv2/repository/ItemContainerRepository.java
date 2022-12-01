package com.gary.backendv2.repository;

import com.gary.backendv2.model.inventory.ItemContainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemContainerRepository extends JpaRepository<ItemContainer, Integer> {
}