package com.example.TradeBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ItemStatus{
    BUY_REQUEST("Buy request"),
    INVENTORY("Inventory"),
    NOT_ENOUGH_MONEY_DURING_BUY("Not enough money during buy"),
    NOT_BUY_FOR_LONG_TIME("Not buy for long time"),
    NOT_SELL_FOR_LONG_TIME("Not sell for long time"),
    SELL_REQUEST("Sell request");
    private final String name;

}
