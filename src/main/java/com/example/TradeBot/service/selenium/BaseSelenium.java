package com.example.TradeBot.service.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class BaseSelenium {
    private final WebDriver driver;

    public BaseSelenium() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(6));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
    }

    public void tearDown(){
        driver.close(); // закриває хром драйвер
//        driver.quit(); // закриває хром
    }

}
