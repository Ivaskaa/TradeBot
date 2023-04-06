package com.example.TradeBot.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseByPage<X> {

    private List<X> data;
    private Long itemsCount;
    private Integer pagesCount;

}
