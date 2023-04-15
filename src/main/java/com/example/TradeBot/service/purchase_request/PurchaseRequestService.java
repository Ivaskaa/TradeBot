package com.example.TradeBot.service.purchase_request;

import com.example.TradeBot.model.PurchaseRequest;

public interface PurchaseRequestService {
    void savePurchaseRequest(PurchaseRequest purchaseRequest);
    boolean isExistByfFullNameAndExterior(String fullName, String exterior);
}
