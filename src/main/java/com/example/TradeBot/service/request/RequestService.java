package com.example.TradeBot.service.request;

import com.example.TradeBot.model.item.Item;

import java.util.List;

public interface RequestService {
    List<Item> getCurrencyFromApi(float startPrice, float endPrice, float userPercentOverprice) throws Exception;
    void getCurrencyFromApiDiscountPercent() throws Exception;
}
