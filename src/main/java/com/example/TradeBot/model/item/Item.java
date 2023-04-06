package com.example.TradeBot.model.item;

import com.example.TradeBot.model.sticker.Sticker;
import java.time.LocalDateTime;
import java.util.List;

public class Item {
    private String shortName;
    private String quality; // 2 букви якості
    private boolean isStatTrak;


    private LocalDateTime purchaseDate;
    private boolean hasTradeLock;
    private Float price;
    //region type values
    // 1 - ключі
    // 2 - ножі
    // 3 - автомати
    // 4 - снайперки
    // 5 пістолети
    // 6 - пп
    // 7 - дробовики
    // 8 - кулемети
    // 9 - якісь значки
    // 10 - наклейки
    // 11 - музика
    //endregion
    private Integer type; // тип елемента
    private List<Sticker> stickers;
}
