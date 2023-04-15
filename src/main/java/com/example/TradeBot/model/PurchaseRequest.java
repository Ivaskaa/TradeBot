package com.example.TradeBot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_requests")
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @CreationTimestamp
    private LocalDateTime purchaseAt;
    private String fullName;
    private float purchasePrice;
    private float sellPrice;
}
