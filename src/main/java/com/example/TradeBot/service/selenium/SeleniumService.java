package com.example.TradeBot.service.selenium;

import java.io.IOException;

public interface SeleniumService {
    void startDriver() throws IOException;
    boolean login() throws InterruptedException, IOException;
    float getBalance();
    void sendPurchaseRequests(float startPrice, float endPrice, float profitPercent) throws InterruptedException;
    void updateInventory();
    void sendSellRequests() throws InterruptedException;
    void endDriver();
}
