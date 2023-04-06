package com.example.TradeBot.dto.request;

import com.example.TradeBot.dto.item.ItemFromParser;
import lombok.Data;

import java.util.List;

@Data
public class RequestListOfItems {
    private List<ItemFromParser> items;
}
