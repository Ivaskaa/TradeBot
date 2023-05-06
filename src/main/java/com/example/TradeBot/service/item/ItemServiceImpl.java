package com.example.TradeBot.service.item;

import com.example.TradeBot.model.Item;
import com.example.TradeBot.model.ItemStatus;
import com.example.TradeBot.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    public void saveItem(Item item) {
        log.info("trying save item: {}", item);
        itemRepository.save(item);
        log.info("success save item");
    }

    @Override
    public List<Item> getAllItemsByStatus(ItemStatus itemStatus) {
        log.info("trying getting all items with status BUY_REQUEST");
        List<Item> items = itemRepository.findAllByStatus(itemStatus);
        log.info("success getting all items with status BUY_REQUEST");
        return items;
    }

    @Override
    public void deleteByFullNameAndExterior(String fullName, String exterior) {
        log.info("trying delete item by fullName: {}, and exterior: {}", fullName, exterior);
        itemRepository.deleteByFullNameAndExterior(fullName, exterior);
        log.info("success delete item");
    }

    @Override
    public Item findItemByFullNameAndExteriorAndStatus(String fullName, String exterior, ItemStatus itemStatus) {
        Item item = itemRepository.findByFullNameAndExteriorAndStatus(fullName, exterior, itemStatus);
        return item;
    }

    @Override
    public boolean isExistByFullNameAndExteriorAndStatusBuyRequest(String fullName, String exterior) {
        return itemRepository.existsByFullNameAndExteriorAndStatus(fullName, exterior, ItemStatus.BUY_REQUEST);
    }

    @Override
    public void changeStatusToInventoryByFullNameAndExteriorAndStatus(String fullName, String exterior, LocalDateTime canSellAt, ItemStatus itemStatus) {
        Item item = itemRepository.findByFullNameAndExteriorAndStatus(fullName, exterior, itemStatus);
        item.setCanSellAt(canSellAt);
        item.setStatus(ItemStatus.INVENTORY);
        itemRepository.save(item);
    }

    @Override
    public void changeStatusToNotEnoughMoneyDuringBuyByFullNameAndExteriorAndStatus(String fullName, String exterior, ItemStatus itemStatus) {
        Item item = itemRepository.findByFullNameAndExteriorAndStatus(fullName, exterior, itemStatus);
        item.setStatus(ItemStatus.NOT_ENOUGH_MONEY_DURING_BUY);
        itemRepository.save(item);
    }

    @Override
    public void changeStatusToSellRequest(Item item, float receivedPrice) {
        item.setReceivedPrice(receivedPrice);
        item.setStatus(ItemStatus.SELL_REQUEST);
        itemRepository.save(item);
    }


}
