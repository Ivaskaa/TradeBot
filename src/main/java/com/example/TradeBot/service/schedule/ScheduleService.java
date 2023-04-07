package com.example.TradeBot.service.schedule;

public interface ScheduleService {
    /**
     * scheduled every day in 8:00
     * request for update currency table
     * @throws Exception
     */
    void getCurrencyFromApi() throws Exception;
}
