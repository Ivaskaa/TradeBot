package com.example.TradeBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "inventory_items")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    private LocalDateTime canSellAt;
    private String fullName;
    private String exterior;
    private float buyPrice;
    private float sellPrice;

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", canSellAt=" + canSellAt +
                ", fullName='" + fullName + '\'' +
                ", exterior='" + exterior + '\'' +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem that = (InventoryItem) o;
        return Float.compare(that.buyPrice, buyPrice) == 0 && Float.compare(that.sellPrice, sellPrice) == 0 && Objects.equals(id, that.id) && Objects.equals(canSellAt, that.canSellAt) && Objects.equals(fullName, that.fullName) && Objects.equals(exterior, that.exterior);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, canSellAt, fullName, exterior, buyPrice, sellPrice);
    }
}
