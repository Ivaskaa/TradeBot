package com.example.TradeBot.dto.item;

import com.example.TradeBot.dto.overpay.OverpayFromParser;
import com.example.TradeBot.dto.sticker.StickerFromParser;
import lombok.Data;

import java.util.List;

@Data
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
    private Integer rank;
    private String collection;
    private List<StickerFromParser> stickers;
    private OverpayFromParser overpay;
    private String inspect;
    private String fullName;
    private Float userPercentOverprice;
    private String shortName;

    @Override
    public String toString() {
        return "ItemFromParser{" +
                "overprice=" + overprice +
                ", price=" + price +
                ", userOverprice=" + userOverprice +
                ", priceWithBonus=" + priceWithBonus +
                ", userPercentOverprice=" + userPercentOverprice +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
