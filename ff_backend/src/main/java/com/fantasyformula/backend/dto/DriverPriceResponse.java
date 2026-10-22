
package com.fantasyformula.backend.dto;

import com.fantasyformula.backend.entity.DriverPrice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record DriverPriceResponse(
        Integer seasonYear,
        String driverExternalId,
        BigDecimal currentPrice,
        OffsetDateTime updatedAt
) {
    public static DriverPriceResponse from(DriverPrice driverPrice) {
        return new DriverPriceResponse(
                driverPrice.getId().getSeasonYear(),
                driverPrice.getId().getDriverExternalId(),
                driverPrice.getCurrentPrice(),
                driverPrice.getUpdatedAt()
        );
    }
}
