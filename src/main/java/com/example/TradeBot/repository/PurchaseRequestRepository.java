package com.example.TradeBot.repository;

import com.example.TradeBot.model.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    boolean existsByFullNameAndExterior(String fullName, String exterior);
    @Modifying
    @Transactional
    void deleteByFullNameAndExterior(String fullName, String exterior);
}
