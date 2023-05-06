package com.example.TradeBot.service.item;

import com.example.TradeBot.model.Item;
import com.example.TradeBot.model.ItemStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemService {
    void saveItem(Item item);
    List<Item> getAllItemsByStatus(ItemStatus itemStatus);
    boolean isExistByFullNameAndExteriorAndStatusBuyRequest(String fullName, String exterior);
    void deleteByFullNameAndExterior(String fullName, String exterior);
    Item findItemByFullNameAndExteriorAndStatus(String fullName, String exterior, ItemStatus inventory);

    void changeStatusToInventoryByFullNameAndExteriorAndStatus(String fullName, String exterior, LocalDateTime canSellAt, ItemStatus itemStatus);
    void changeStatusToNotEnoughMoneyDuringBuyByFullNameAndExteriorAndStatus(String fullName, String exterior, ItemStatus itemStatus);
    void changeStatusToSellRequest(Item inventoryItem, float receivedPrice);
}
