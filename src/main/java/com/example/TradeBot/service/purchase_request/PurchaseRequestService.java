package com.example.TradeBot.service.purchase_request;

import com.example.TradeBot.model.PurchaseRequest;

import java.util.List;

public interface PurchaseRequestService {
    void savePurchaseRequest(PurchaseRequest purchaseRequest);
    List<PurchaseRequest> getAllPurchaseRequest();
    boolean isExistByfFullNameAndExterior(String fullName, String exterior);
    void deleteByFullNameAndExterior(String fullName, String exterior);
}
