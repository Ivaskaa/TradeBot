package com.example.TradeBot;

import com.example.TradeBot.service.item.ItemService;
import com.example.TradeBot.service.request.RequestService;
import com.example.TradeBot.service.selenium.SeleniumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class Init implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final ItemService itemService;
    private final SeleniumService seleniumService;

    private final RequestService requestService;

    @Override
    public void run(String... args) throws Exception {
        log.info("################## START OF INITIALIZATION ##################");
//        requestService.getCurrencyFromApi(1.5f, 5.5f, 20.0f);
        seleniumService.startDriver();
        seleniumService.login();
        System.out.println(seleniumService.getBalance());
        log.info("################## END OF INITIALIZATION ##################");
    }


}
