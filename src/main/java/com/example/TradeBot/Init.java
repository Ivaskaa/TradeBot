package com.example.TradeBot;

import com.example.TradeBot.service.item.ItemService;
import com.example.TradeBot.service.request.RequestService;
import com.example.TradeBot.service.selenium.SeleniumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;

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
//        List<Item> items = requestService.getCurrencyFromApi(2.5f, 5.5f, 12.0f);
//        seleniumService.startDriver();
//        seleniumService.buyItems(items, 100f);
//        requestService.getCurrencyFromApiDiscountPercent();
        HttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder uriBuilder = new URIBuilder("https://cs.money/price_changes?appId=730&nameId=1140&startTime=1678470198976&endTime=1681062198976");
        URI uri = uriBuilder.build();
        log.info("url: {}", uri);
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getEntity());
        log.info("################## END OF INITIALIZATION ##################");
    }


}
