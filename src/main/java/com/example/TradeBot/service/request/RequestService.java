package com.example.TradeBot.service.request;

public interface RequestService {
    void getCurrencyFromApi(float startPrice, float endPrice, float userPercentOverprice) throws Exception;
}
