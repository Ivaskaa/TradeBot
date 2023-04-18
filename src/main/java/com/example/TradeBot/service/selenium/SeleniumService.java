package com.example.TradeBot.service.selenium;

import java.io.IOException;

public interface SeleniumService {
    void startDriver() throws IOException;
    boolean login() throws InterruptedException, IOException;
    float getBalance();
    void getElementsToBuy() throws InterruptedException;
    void updateInventory();
    void getBuyOrders();
}
