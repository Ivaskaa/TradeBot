package com.example.TradeBot;

import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.dto.request.RequestListOfItems;
import com.example.TradeBot.service.item.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.failsafe.Execution;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class Init implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final ItemService itemService;

    @Override
    public void run(String... args) throws Exception {
        log.info("################## START OF INITIALIZATION ##################");

        log.info("################## END OF INITIALIZATION ##################");
    }


}
