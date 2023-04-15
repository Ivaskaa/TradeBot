package com.example.TradeBot;

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

    private final SeleniumService seleniumService;


    @Override
    public void run(String... args) throws Exception {
        log.info("################## START OF INITIALIZATION ##################");
        seleniumService.startDriver();
        seleniumService.login();
        seleniumService.getElementsToBuy();
        log.info("################## END OF INITIALIZATION ##################");
    }


}
