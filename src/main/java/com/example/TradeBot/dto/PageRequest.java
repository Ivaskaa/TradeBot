package com.example.TradeBot.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageRequest {
    @NotNull
    @Min(value = 0, message = "{page.validation}")
    private Integer page;
}
