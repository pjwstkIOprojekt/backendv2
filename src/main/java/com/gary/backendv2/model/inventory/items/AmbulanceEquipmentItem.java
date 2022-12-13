package com.gary.backendv2.model.inventory.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class AmbulanceEquipmentItem extends Item {
    private String name;
    private String manufacturer;
    private String description;
}
