package com.example.TradeBot.service.schedule;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.dto.request.RequestListOfItems;
import com.example.TradeBot.service.item.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Log4j2
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    @Override
    // https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
    @Scheduled(cron = "0 30 * * * *") // (в 1:30, 2:30, 3:30, и т.д.).
    public void getCurrencyFromApi() throws Exception {

        List<ItemFromParser> itemsFromParser = new ArrayList<>();

        for(int type = 3; type <= 4; type++){
            int offset;
            if(type == 3){
                offset = 1500;
            } else {
                offset = 800;
            }
            float startPrice = 1.5f;
            float endPrice = 5.5f;
            float userPercentOverprice = 20.0f;
            boolean isBreak = false;

            for(; offset < 5000; offset += 60){
                HttpClient httpClient = HttpClientBuilder.create().build();
                URIBuilder uriBuilder = new URIBuilder("https://inventories.cs.money/5.0/load_bots_inventory/730");
                uriBuilder.addParameter("limit", "60");
                uriBuilder.addParameter("offset", String.valueOf(offset));
                uriBuilder.addParameter("order", "asc");
                //uriBuilder.addParameter("priceWithBonus", "30");
                uriBuilder.addParameter("sort", "price");
                uriBuilder.addParameter("type", String.valueOf(type));
                //uriBuilder.addParameter("withStack", "true");

                URI uri = uriBuilder.build();
                System.out.println(uri);
                HttpGet httpGet = new HttpGet(uri);
                HttpResponse response = httpClient.execute(httpGet);
                RequestListOfItems requestListOfItems = objectMapper.readValue(EntityUtils.toString(response.getEntity()), RequestListOfItems.class);
                for (ItemFromParser item : requestListOfItems.getItems()){
                    if(item.getPrice() > startPrice){
                        if(item.getPrice() > endPrice){
                            isBreak = true;
                        } else if (!Objects.isNull(item.getUserPercentOverprice()) && // переоцінка користувача не порожня
                                item.getUserPercentOverprice() < userPercentOverprice && // допустима переоцінка користувача
                                !item.isHasTradeLock() && // доступна транзакція
                                // перевірка на можливіть поставити націнку > 20%
                                !Objects.isNull(item.getOverpay()) // стікери або флоат
                        )
                        {
                            itemsFromParser.add(item);
                            System.out.println(item);
                        }
                    }
                }
                if(isBreak) break;
                Thread.sleep(300);
            }
        }

        itemService.saveList(itemsFromParser);
    }
}
