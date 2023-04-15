package com.example.TradeBot.repository;

import com.example.TradeBot.model.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    boolean existsByFullNameAndExterior(String fullName, String exterior);
}
