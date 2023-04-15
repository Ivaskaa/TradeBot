package com.example.TradeBot.service.purchase_request;

import com.example.TradeBot.model.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestServiceImpl implements PurchaseRequestService {
    @Override
    public void saveNewPurchaseRequest(PurchaseRequest purchaseRequest) {
        log.info("save purchase request: {}", purchaseRequest);

    }
}
