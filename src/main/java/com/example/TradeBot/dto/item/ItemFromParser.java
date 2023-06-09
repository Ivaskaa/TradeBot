package com.example.TradeBot.dto.item;

import com.example.TradeBot.dto.overpay.OverpayFromParser;
import com.example.TradeBot.dto.sticker.StickerFromParser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)

public class ItemFromParser {
    private Long appId;
    private Long assetId;
    private boolean hasHighDemand;
    private boolean hasTradeLock;
    private Long id;
    private String img;
    private boolean isMarket;
    private boolean isStatTrak;
    private Long nameId;
    private Float overprice; // націнка бажано щоб була маленька
    private Float price;
    private String quality;
    private String rarity;
    private String steamId;
    private String steamImg;
    private Integer type;
    private Float userOverprice;
    private String preview; // силка
    private String screenshot; // силка
    private Float priceWithBonus;
    private Long userId;
    private Integer pattern; // номер паттерна
    private String rank;
    private Integer backSide;
    private Integer playSide;
    private String collection;
    private OverpayFromParser overpay;
    private List<StickerFromParser> stickers;
    private String inspect;
    private String fullName;
    private Float userPercentOverprice;
    private boolean hasConcreteSkinPage;
    private String shortName;

    @Override
    public String toString() {
        return "ItemFromParser{" +
                ", price=" + price +
                ", userPercentOverprice=" + userPercentOverprice +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
