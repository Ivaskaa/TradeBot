package com.example.TradeBot.service.item;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.mapper.ItemMapper;
import com.example.TradeBot.model.item.Item;
import com.example.TradeBot.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public void save(Item item) {
        log.info("save item: {}", item);
        itemRepository.save(item);
        log.info("success save item");
    }
    @Override
    public void saveList(List<ItemFromParser> itemsFormParser) {
        log.info("save items from parser");
        List<Item> items = itemsFormParser
                .stream()
                .map(itemFromParser -> {
                    Item item = itemMapper.itemFromParserToItem(itemFromParser);
                    item.setDate(LocalDateTime.now());
                    return item;
                })
                .toList();

        itemRepository.saveAll(items);
        log.info("success save items");
    }
}
