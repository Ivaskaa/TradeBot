package com.example.TradeBot.service.schedule;

import com.example.TradeBot.service.request.RequestService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final RequestService requestsService;

    @Override
    // https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
    @Scheduled(cron = "0 30 * * * *") // (в 1:30, 2:30, 3:30, и т.д.).
    public void getCurrencyFromApi() throws Exception {
        requestsService.getCurrencyFromApi(1.5f, 5.5f, 20.0f);
    }
}
