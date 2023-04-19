package com.example.TradeBot;

import com.example.TradeBot.service.selenium.SeleniumService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@AllArgsConstructor
public class Init implements CommandLineRunner {

    private final SeleniumService seleniumService;

    @Override
    public void run(String... args) throws Exception {
        log.info("################## START OF INITIALIZATION ##################");
        System.out.println(LocalDateTime.now());
        seleniumService.startDriver();
        boolean successLogin = seleniumService.login();
        if(successLogin){
//            seleniumService.updateInventory();
//            seleniumService.sendPurchaseRequests(100f, 200f, 5f);
            seleniumService.sendSellRequests();
        }
//        seleniumService.endDriver();
        log.info("################## END OF INITIALIZATION ##################");
    }


}
