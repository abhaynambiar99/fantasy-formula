
package com.fantasyformula.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "driver_prices")
public class DriverPrice {

    @EmbeddedId
    private DriverPriceId id;

    @Column(name = "current_price", nullable = false, precision = 6, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public DriverPrice() {
    }

    public DriverPrice(
            DriverPriceId id,
            BigDecimal currentPrice,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.currentPrice = currentPrice;
        this.updatedAt = updatedAt;
    }

    public DriverPriceId getId() {
        return id;
    }

    public void setId(DriverPriceId id) {
        this.id = id;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
