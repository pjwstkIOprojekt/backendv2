package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.ItemCountUnit;
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

    private String name;

    @Enumerated(EnumType.STRING)
    private ItemCountUnit unit;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ItemGroup itemGroup;

}
