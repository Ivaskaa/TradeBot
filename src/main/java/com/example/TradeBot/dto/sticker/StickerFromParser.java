package com.example.TradeBot.dto.sticker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class StickerFromParser {
    private Float overprice;
    private String name;
    private Integer position;
    private Float price;
    private Integer wear;
    private String img; // силка на картинку
    private String wikiLink; // силка на вікі
}
