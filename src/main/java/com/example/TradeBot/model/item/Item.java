package com.example.TradeBot.model.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
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
    // 5 - пістолети
    // 6 - пп
    // 7 - дробовики
    // 8 - кулемети
    // 9 - якісь значки
    // 10 - наклейки
    // 11 - музика
    //endregion
    private Integer type; // тип елементи



//    @Getter
//    @RequiredArgsConstructor
//    public enum Quality{
//        WW,FT,ST,FN,BS
//        private final String value;
//    }
}
