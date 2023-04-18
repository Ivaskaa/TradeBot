package com.example.TradeBot.service.purchase_request;

import com.example.TradeBot.model.PurchaseRequest;
import com.example.TradeBot.repository.PurchaseRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestServiceImpl implements PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    @Override
    public void savePurchaseRequest(PurchaseRequest purchaseRequest) {
        log.info("trying save purchase request: {}", purchaseRequest);
        purchaseRequestRepository.save(purchaseRequest);
        log.info("success save purchase request");
    }

    @Override
    public List<PurchaseRequest> getAllPurchaseRequest() {
        log.info("trying getting all purchase requests");
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        log.info("success getting all purchase requests");
        return purchaseRequestList;
    }

    @Override
    public void deleteByFullNameAndExterior(String fullName, String exterior) {
        log.info("trying delete purchase request by fullName: {}, and exterior: {}", fullName, exterior);
        purchaseRequestRepository.deleteByFullNameAndExterior(fullName, exterior);
        log.info("success delete purchase request");
    }

    @Override
    public boolean isExistByfFullNameAndExterior(String fullName, String exterior) {
        return purchaseRequestRepository.existsByFullNameAndExterior(fullName, exterior);
    }
}
