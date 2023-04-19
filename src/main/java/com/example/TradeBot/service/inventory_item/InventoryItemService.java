package com.example.TradeBot.service.inventory_item;

import com.example.TradeBot.model.InventoryItem;

public interface InventoryItemService {
    void saveInventoryItem(InventoryItem inventoryItem);

    InventoryItem findInventoryItemByFullNameAndExterior(String fullName, String exterior);
}
