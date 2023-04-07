package com.example.TradeBot.mapper;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.model.item.Item;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ItemMapper {

    public Item itemFromParserToItem(ItemFromParser itemFromParser){
        Item item = new Item();
        item.setId(itemFromParser.getId());
        item.setShortName(itemFromParser.getShortName());
        item.setQuality(itemFromParser.getQuality());
        item.setStatTrak(itemFromParser.isStatTrak());
        item.setDate(LocalDateTime.now());
        item.setPrice(itemFromParser.getPrice());
        item.setType(itemFromParser.getType());


        return item;
    }
}
