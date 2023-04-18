package com.example.TradeBot.service.inventory_item;

import com.example.TradeBot.model.InventoryItem;
import com.example.TradeBot.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    @Override
    public void saveInventoryItem(InventoryItem inventoryItem) {
        log.info("trying save inventory item: {}", inventoryItem);
        inventoryItemRepository.save(inventoryItem);
        log.info("success save inventory item");
    }
}
