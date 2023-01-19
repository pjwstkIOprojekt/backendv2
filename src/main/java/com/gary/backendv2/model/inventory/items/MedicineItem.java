package com.gary.backendv2.model.inventory.items;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class MedicineItem extends Item {
    private String name;
    private String manufacturer;
    private String description;
    private LocalDate expirationDate;

    public void accept(EntityVisitor ev, ItemService itemService, List<BaseRequest> baseRequests) {
        ev.visit(this, itemService, baseRequests);
    }
}
