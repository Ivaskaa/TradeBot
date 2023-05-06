package com.example.TradeBot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    @CreationTimestamp
    private LocalDateTime buyRequestAt;
    private LocalDateTime canSellAt;
    private LocalDateTime sellRequestAt;
    private String fullName;
    private String exterior;
    private float sellPrice;
    private float buyPrice;
    private float receivedPrice;
    private float profitPrice;


    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatus status;


}
