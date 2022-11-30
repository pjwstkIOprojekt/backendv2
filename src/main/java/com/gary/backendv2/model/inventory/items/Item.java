package com.gary.backendv2.model.inventory.items;

import com.gary.backendv2.model.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Transient
    public ItemType getDiscriminatorValue() {
        return ItemType.fromClass(this.getClass());
    }
}
