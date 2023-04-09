package com.example.TradeBot.service.selenium;

import com.example.TradeBot.dto.item.ItemFromParser;

import java.io.IOException;
import java.util.List;

public interface SeleniumService {
    void login() throws InterruptedException, IOException;
    void startDriver() throws IOException;
    float getBalance();
    List<ItemFromParser> getInventoryInNewTab();
}
