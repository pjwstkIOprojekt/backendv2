package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.inventory.ItemContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentResponse {
    @Getter
    @Setter
    public static class ItemContainerResponse {
        public ItemContainerResponse(ItemContainer container) {
            this.count = container.getCount();
            this.unit = container.getUnit();
            this.updatedAt = container.getUpdatedAt();
        }

        private Integer count;
        private ItemContainer.Unit unit;
        private LocalDateTime updatedAt;
    }

    private AbstractItemResponse item;
    private ItemContainerResponse itemData;
}
