package com.example.TradeBot.dto.inventory;

import com.example.TradeBot.dto.item.ItemFromParser;
import lombok.Data;
import java.util.List;

@Data
public class InventoryFromParser {
    private List<ItemFromParser> items;
    private int total;
    private double cost;
}
