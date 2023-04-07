package com.example.TradeBot.pages;

import com.example.TradeBot.service.selenium.BaseSelenium;
import org.openqa.selenium.By;

public class MainPage extends BaseSelenium {
    private By login = By.xpath("//*[@id=\"layout-page-header\"]/div[1]/div/div[6]/a/button");
}
