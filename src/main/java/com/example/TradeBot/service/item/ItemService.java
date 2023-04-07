package com.example.TradeBot.service.item;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.model.item.Item;

import java.util.List;

public interface ItemService {
    void save(Item item);
    void saveList(List<ItemFromParser> items);
}
