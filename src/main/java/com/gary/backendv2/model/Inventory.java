package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.ItemType;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "item_group_mapping",
            joinColumns = {@JoinColumn(name = "inventory_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")})
    @MapKeyColumn(name = "item_type")
    private Map<ItemType, ItemGroup> itemMapping = new HashMap<>();

}
