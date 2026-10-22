
package com.fantasyformula.backend.controller;

import com.fantasyformula.backend.service.DriverPriceInitializationService;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/driver-prices")
@Profile("local")
public class DriverPriceInitializationController {

    private final DriverPriceInitializationService initializationService;

    public DriverPriceInitializationController(
            DriverPriceInitializationService initializationService) {
        this.initializationService = initializationService;
    }

    // Explicitly initialize driver prices for a season.
    @PostMapping("/initialize")
    public String initializePrices(
            @RequestParam(defaultValue = "2026") int season,
            @RequestParam(defaultValue = "2025") int historicalSeason) {

        int count = initializationService.initializePrices(
                season, historicalSeason);

        return "Successfully initialized " + count
                + " driver prices for season " + season;
    }
}
