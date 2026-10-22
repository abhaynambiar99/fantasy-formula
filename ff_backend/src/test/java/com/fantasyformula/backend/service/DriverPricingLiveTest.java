
package com.fantasyformula.backend.service;

import com.fantasyformula.backend.integration.jolpica.JolpicaClient;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DriverPricingLiveTest {

    @Test
    void calculatesPricesUsingRealHistoricalStandings() throws Exception {

        // Create a client that connects to the real Jolpica API.
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.jolpi.ca/ergast/f1")
                .build();

        JolpicaClient jolpicaClient = new JolpicaClient(restClient);

        // Load the real 2026 eligible-driver configuration.
        FantasyDriverEligibilityService eligibilityService = new FantasyDriverEligibilityService(
                new ObjectMapper(),
                new ClassPathResource("fantasy-drivers-2026.json"));

        // Create the pricing service with the live API client.

        DriverPricingService pricingService = new DriverPricingService(jolpicaClient, eligibilityService);

        // Salary estimates are deliberately omitted for this first check.
        // Missing salary data uses the service's neutral salary score.
        Map<String, BigDecimal> prices = pricingService.calculateInitialPrices(
                2025,
                eligibilityService.getEligibleDriverIds(),
                Map.of());

        //System.out.println("Rookie IDs: " + eligibilityService.getRookieDriverIds());
        // Confirm every eligible driver received a price.
        assertEquals(
                eligibilityService.getEligibleDriverIds().size(),
                prices.size());

        // Print prices so we can review the distribution.
        prices.forEach((driverId, price) -> System.out.println(driverId + " = $" + price + "M"));

        // Confirm all prices remain within the configured limits.
        prices.values().forEach(price -> {
            assertTrue(price.compareTo(new BigDecimal("4.00")) >= 0);
            assertTrue(price.compareTo(new BigDecimal("40.00")) <= 0);
        });
    }
}
