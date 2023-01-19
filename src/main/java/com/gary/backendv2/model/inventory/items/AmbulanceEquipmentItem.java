package com.gary.backendv2.model.inventory.items;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

@Getter
@Setter
@Entity
public class AmbulanceEquipmentItem extends Item {
    private String name;
    private String manufacturer;
    private String description;

    public void accept(EntityVisitor ev, ItemService itemService, List<BaseRequest> baseRequests) {
        ev.visit(this, itemService, baseRequests);
    }
}
