package com.example.TradeBot.service.request;

import com.example.TradeBot.model.item.Item;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface RequestService {
    List<Item> getCurrencyFromApi(float startPrice, float endPrice, float userPercentOverprice) throws Exception;
    void getCurrencyFromApiDiscountPercent() throws Exception;
    Pair<Double, Double> getMaxAndAveragePriceFromApi(long nameId) throws URISyntaxException, IOException;
}
