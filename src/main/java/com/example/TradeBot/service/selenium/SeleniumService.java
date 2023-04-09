package com.example.TradeBot.service.selenium;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface SeleniumService {
    void login() throws InterruptedException, IOException;
    void startDriver() throws IOException;
    float getBalance();
    List<ItemFromParser> getInventoryInNewTab() throws JsonProcessingException;
    CookieStore getCookies();
    List<ItemFromParser> getInventoryByRequest() throws URISyntaxException, IOException;
}
