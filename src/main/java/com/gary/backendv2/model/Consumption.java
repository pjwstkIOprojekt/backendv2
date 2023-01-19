package com.gary.backendv2.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumption_of_materials_id")
    private ConsumptionOfMaterials consumptionOfMaterials;

    private Integer itemId;

    private Integer count;
}
