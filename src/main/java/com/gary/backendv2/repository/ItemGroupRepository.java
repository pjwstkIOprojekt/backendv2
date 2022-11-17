package com.gary.backendv2.repository;

import com.gary.backendv2.model.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemGroupRepository extends JpaRepository<ItemGroup, Integer> {
}