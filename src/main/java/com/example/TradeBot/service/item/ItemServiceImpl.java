package com.example.TradeBot.service.item;

import com.example.TradeBot.model.item.Item;
import com.example.TradeBot.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    @Override
    public void create(Item item) {
        log.info("create item: {}", item);
        itemRepository.save(item);
        log.info("success save item");
    }
}
