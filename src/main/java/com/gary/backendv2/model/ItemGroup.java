package com.gary.backendv2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ItemGroup {
    public ItemGroup(Inventory inventory) {
        this.inventory = inventory;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;

    @ManyToOne
    private Inventory inventory;

    @OneToMany(mappedBy = "itemGroup", orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
}
