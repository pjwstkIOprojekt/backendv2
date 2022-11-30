package com.gary.backendv2.model.inventory;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;


    @OneToMany
    private Map<Integer, ItemContainer> items = new HashMap<>();
}
