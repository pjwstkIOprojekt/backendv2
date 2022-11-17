package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.ItemCountUnit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
public class ConsumableItem extends Item {
    private Integer amount;
}
