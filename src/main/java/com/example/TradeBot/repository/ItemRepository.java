package com.example.TradeBot.repository;

import com.example.TradeBot.model.Item;
import com.example.TradeBot.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByFullNameAndExteriorAndStatus(String fullName, String exterior, ItemStatus itemStatus);
    List<Item> findAllByStatus(ItemStatus itemStatus);
    @Modifying
    @Transactional
    void deleteByFullNameAndExterior(String fullName, String exterior);

    Item findByFullNameAndExteriorAndStatus(String fullName, String exterior, ItemStatus buyRequest);
    Item findByFullNameAndExteriorAndCanSellAtAndStatus(String fullName, String exterior, LocalDateTime canSellAt, ItemStatus status);
}
