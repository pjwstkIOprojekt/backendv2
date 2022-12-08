package com.gary.backendv2.model.inventory.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class MultiUseItem extends Item {
    String name;

    // TODO: what else
}
