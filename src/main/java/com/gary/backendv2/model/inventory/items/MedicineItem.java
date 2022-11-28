package com.gary.backendv2.model.inventory.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class MedicineItem extends Item {
    private String name;
    private String manufacturer;
    private String description;
    private LocalDate expirationDate;
}
