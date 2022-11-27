package com.gary.backendv2.model.inventory;

import com.gary.backendv2.model.inventory.items.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class ItemContainer {
    public ItemContainer() {
        this.updatedAt = LocalDateTime.now();
        this.count = 0;
    }

    public enum Unit {
        COUNT, VOLUME, BOX
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemContainerId;

    private Integer count;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private LocalDateTime updatedAt;

    public void incrementCount() {
        this.count++;
        this.updatedAt = LocalDateTime.now();
    }

    public void decrementCount() {
        this.count--;
        this.updatedAt = LocalDateTime.now();
    }
}
