package com.example.TradeBot.dto.sticker;

import lombok.Data;

@Data
public class StickerFromParser {
    private Float overprice;
    private String name;
    private Integer position;
    private Float price;
    private Integer wear;
    private String img; // силка на картинку
    private String wikiLink; // силка на вікі
}
