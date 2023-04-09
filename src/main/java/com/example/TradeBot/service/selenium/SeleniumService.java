package com.example.TradeBot.service.selenium;

import com.example.TradeBot.model.item.Item;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.util.List;
import java.net.URISyntaxException;
import java.util.List;

public interface SeleniumService {
    void login() throws InterruptedException, IOException;
    void startDriver() throws IOException;
    float getBalance();
    void buyItems(List<Item> items, float balance) throws InterruptedException;
    boolean buyItem(long number, Float price) throws InterruptedException;
    void changeSelectToDiscount() throws InterruptedException;
    List<ItemFromParser> getInventoryInNewTab() throws JsonProcessingException;
    CookieStore getCookies();
    List<ItemFromParser> getInventoryByRequest() throws URISyntaxException, IOException;
}
