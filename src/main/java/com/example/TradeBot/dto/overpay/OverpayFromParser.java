package com.example.TradeBot.dto.overpay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class OverpayFromParser {
    private Float stickers;
    private Float float_;
}
