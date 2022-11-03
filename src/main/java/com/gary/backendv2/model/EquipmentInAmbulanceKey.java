package com.gary.backendv2.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class EquipmentInAmbulanceKey implements Serializable {

    @Column(name = "ambulanceId")
    Integer ambulanceId;

    @Column(name = "equipmentId")
    Integer equipmentId;

}
