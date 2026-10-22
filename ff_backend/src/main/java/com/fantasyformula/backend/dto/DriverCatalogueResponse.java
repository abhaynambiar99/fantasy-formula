
package com.fantasyformula.backend.dto;

import com.fantasyformula.backend.integration.jolpica.dto.JolpicaDriver;

import java.math.BigDecimal;

public record DriverCatalogueResponse(
        String driverId,
        String code,
        String givenName,
        String familyName,
        String nationality,
        String permanentNumber,
        BigDecimal currentPrice
) {
    public static DriverCatalogueResponse from(
            JolpicaDriver driver,
            BigDecimal currentPrice) {

        return new DriverCatalogueResponse(
                driver.driverId(),
                driver.code(),
                driver.givenName(),
                driver.familyName(),
                driver.nationality(),
                driver.permanentNumber(),
                currentPrice
        );
    }
}
