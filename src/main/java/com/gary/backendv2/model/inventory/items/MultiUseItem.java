package com.gary.backendv2.model.inventory.items;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

@Getter
@Setter
@Entity
public class MultiUseItem extends Item {
    String name;

    public void accept(EntityVisitor ev, ItemService itemService, List<BaseRequest> baseRequests) {
        ev.visit(this, itemService, baseRequests);
    }
}
