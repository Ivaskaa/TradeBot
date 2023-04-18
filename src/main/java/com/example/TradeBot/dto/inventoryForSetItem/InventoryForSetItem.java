package com.example.TradeBot.dto.inventoryForSetItem;

import lombok.Data;

import java.util.Objects;

@Data
public class InventoryForSetItem {
    private String name;
    private String exterior;
    private String tradableAfter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryForSetItem item = (InventoryForSetItem) o;
        return Objects.equals(name, item.name) && Objects.equals(exterior, item.exterior) && Objects.equals(tradableAfter, item.tradableAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, exterior, tradableAfter);
    }
}
