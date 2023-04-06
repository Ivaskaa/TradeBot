package com.example.TradeBot.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ObjectDto {
    @NotNull
    private Long id;
}