package com.example.TradeBot.service.request;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.dto.request.RequestListOfItems;
import com.example.TradeBot.mapper.ItemMapper;
import com.example.TradeBot.model.item.Item;
import com.example.TradeBot.service.item.ItemService;
import com.example.TradeBot.service.selenium.SeleniumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final ItemService itemService;
    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;
    private final SeleniumService seleniumService;

    @Override
    public List<Item> getCurrencyFromApi(float startPrice, float endPrice, float userPercentOverprice) throws Exception {

        List<ItemFromParser> itemsFromParser = new ArrayList<>();

        for(int type = 3; type <= 4; type++){
            int offset;
            if(type == 3){
                offset = 2000;
            } else {
                offset = 800;
            }
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
                        ) {
                            itemsFromParser.add(item);
                            System.out.println(item);
                        }
                    }
                }
                if(isBreak) break;
                Thread.sleep(300);
            }
        }

        List<Item> items = itemsFromParser
                .stream()
                .map(itemFromPar -> {
                    Item item = itemMapper.itemFromParserToItem(itemFromPar);
                    item.setDate(LocalDateTime.now());
                    return item;
                })
                .toList();

        itemService.saveList(items);
        return items;
    }

    public void getCurrencyFromApiDiscountPercent() throws Exception {

        seleniumService.startDriver();
        seleniumService.login();
//        float balance = seleniumService.getBalance();
        seleniumService.changeSelectToDiscount();
        List<Long> listOfBadItems = new ArrayList<>();

        while(true){
            HttpClient httpClient = HttpClientBuilder.create().build();
            URIBuilder uriBuilder = new URIBuilder("https://inventories.cs.money/5.0/load_bots_inventory/730");
            uriBuilder.addParameter("limit", "25");
            uriBuilder.addParameter("offset", "0");
            uriBuilder.addParameter("order", "asc");
            uriBuilder.addParameter("sort", "discount_percent");
            URI uri = uriBuilder.build();
            log.info("url: {}", uri);
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);
            RequestListOfItems requestListOfItems = objectMapper.readValue(EntityUtils.toString(response.getEntity()), RequestListOfItems.class);
            long number = 1;
            for (ItemFromParser itemFromParser : requestListOfItems.getItems()){
                number++;
                boolean isBreak = false;
                for(Long badItemId : listOfBadItems){
                    if(Objects.equals(itemFromParser.getId(), badItemId)){
                        isBreak = true;
                        break;
                    }
                }
                if(isBreak) break;
                if (Objects.nonNull(itemFromParser.getUserPercentOverprice()) && // переоцінка користувача не порожня
                        itemFromParser.getUserPercentOverprice() < -8.5f  && // допустима знижка
                        !itemFromParser.isHasTradeLock() && // доступна транзакція
                        itemFromParser.getPrice() < 33.00 &&
                        itemFromParser.getPrice() > 2.20
                ) {
                    System.out.println(itemFromParser);
                    boolean success = seleniumService.buyItem(number, itemFromParser.getPrice());
                    if(!success){
                        listOfBadItems.add(itemFromParser.getId());
                    }
                }
            }
        }

    }
}
