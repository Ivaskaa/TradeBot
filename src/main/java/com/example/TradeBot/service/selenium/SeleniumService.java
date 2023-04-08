package com.example.TradeBot.service.selenium;

import java.io.IOException;

public interface SeleniumService {
    void login() throws InterruptedException, IOException;
    void startDriver() throws IOException;
    float getBalance();
}
