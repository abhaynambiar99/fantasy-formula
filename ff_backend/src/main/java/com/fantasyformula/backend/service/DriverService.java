
package com.fantasyformula.backend.service;

import com.fantasyformula.backend.dto.DriverCatalogueResponse;
//import com.fantasyformula.backend.entity.DriverPrice;
import com.fantasyformula.backend.integration.jolpica.JolpicaClient;
import com.fantasyformula.backend.integration.jolpica.dto.JolpicaDriver;
import com.fantasyformula.backend.repository.DriverPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//import java.util.Objects;

@Service
public class DriverService {

    private final JolpicaClient jolpicaClient;
    private final DriverPriceRepository driverPriceRepository;
    private final FantasyDriverEligibilityService eligibilityService;

   public DriverService(
        JolpicaClient jolpicaClient,
        DriverPriceRepository driverPriceRepository,
        FantasyDriverEligibilityService eligibilityService) {

    this.jolpicaClient = jolpicaClient;
    this.driverPriceRepository = driverPriceRepository;
    this.eligibilityService = eligibilityService;
}

    @Transactional(readOnly = true)
    public List<DriverCatalogueResponse> getDriversBySeason(Integer season) {
        if (season == null || season < 1950 || season > 2100) {
            throw new IllegalArgumentException("Invalid season year");
        }

        List<JolpicaDriver> drivers =
                jolpicaClient.getDriversBySeason(season);

        Map<String, BigDecimal> prices =
                driverPriceRepository
                        .findByIdSeasonYearOrderByCurrentPriceDesc(season)
                        .stream()
                        .collect(Collectors.toMap(
                                price -> price.getId().getDriverExternalId(),
                                price -> price.getCurrentPrice()
                        ));

        return drivers.stream()
        .filter(driver -> eligibilityService.isEligible(driver.driverId()))
        .map(driver -> DriverCatalogueResponse.from(
                driver,
                prices.get(driver.driverId())
        ))
        .toList();
    }
}
