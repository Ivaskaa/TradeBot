package com.example.TradeBot;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.dto.request.RequestListOfItems;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class Init implements CommandLineRunner {

    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("################## START OF INITIALIZATION ##################");

        Integer offset = 2000;

        for(;offset <= 3000; offset+= 60){
            HttpClient httpClient = HttpClientBuilder.create().build();
            URIBuilder uriBuilder = new URIBuilder("https://inventories.cs.money/5.0/load_bots_inventory/730?limit=60&offset="+ offset +"&order=asc&priceWithBonus=30&sort=price&type=3&withStack=true");

            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);
            RequestListOfItems requestListOfItems = objectMapper.readValue(EntityUtils.toString(response.getEntity()), RequestListOfItems.class);
            for (ItemFromParser item : requestListOfItems.getItems()){
                if(Objects.nonNull(item.getOverprice()) && !item.isHasTradeLock() && !item.isHasHighDemand()) {
                    System.out.println(item);
                }
            }
            Thread.sleep(300);
        }

        log.info("################## END OF INITIALIZATION ##################");
    }


}
