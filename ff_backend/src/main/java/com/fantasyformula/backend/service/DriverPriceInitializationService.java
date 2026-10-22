
package com.fantasyformula.backend.service;

import com.fantasyformula.backend.entity.DriverPrice;
import com.fantasyformula.backend.entity.DriverPriceId;
import com.fantasyformula.backend.repository.DriverPriceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class DriverPriceInitializationService {

    // Calculate prices using the existing pricing logic.
    private final DriverPricingService pricingService;

    // Read and write the stored driver prices.
    private final DriverPriceRepository driverPriceRepository;

    // Provides the configured list of eligible drivers and rookies.
    private final FantasyDriverEligibilityService eligibilityService;

    public DriverPriceInitializationService(
            DriverPricingService pricingService,
            DriverPriceRepository driverPriceRepository,
            FantasyDriverEligibilityService eligibilityService) {

        this.pricingService = pricingService;
        this.driverPriceRepository = driverPriceRepository;
        this.eligibilityService = eligibilityService;
    }

    // Initialize prices in one transaction.
    // If a database operation fails, the inserts are rolled back.
    @Transactional
    public int initializePrices(int season, int historicalSeason) {

        // Get the eligible drivers configured for the current season.
        Set<String> eligibleDriverIds =
                eligibilityService.getEligibleDriverIds();

        // Refuse to initialize if prices already exist for this season.
        // This prevents accidentally overwriting existing prices.
        if (!driverPriceRepository
                .findByIdSeasonYearOrderByCurrentPriceDesc(season)
                .isEmpty()) {

            throw new IllegalStateException(
                    "Prices already exist for season " + season
                            + ". Initialization cancelled.");
        }

        // Calculate prices without saving them automatically.
        // No verified salary estimates are available yet, so use an empty map.
        Map<String, BigDecimal> initialPrices =
                pricingService.calculateInitialPrices(
                        historicalSeason,
                        eligibleDriverIds,
                        Map.of());

        // Confirm that every eligible driver received a calculated price.
        if (initialPrices.size() != eligibleDriverIds.size()
                || !initialPrices.keySet().equals(eligibleDriverIds)) {

            throw new IllegalStateException(
                    "Calculated prices do not match the eligible driver list.");
        }

        // Create one database entity for each driver's initial price.
        OffsetDateTime now = OffsetDateTime.now();

        for (Map.Entry<String, BigDecimal> entry
                : initialPrices.entrySet()) {

            DriverPrice driverPrice = new DriverPrice(
                    new DriverPriceId(season, entry.getKey()),
                    entry.getValue(),
                    now);

            // Save the price inside the current transaction.
            driverPriceRepository.save(driverPrice);
        }

        // Return the number of initialized driver prices.
        return initialPrices.size();
    }
}
