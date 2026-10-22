
package com.fantasyformula.backend.service;

import com.fantasyformula.backend.integration.jolpica.JolpicaClient;
import com.fantasyformula.backend.integration.jolpica.dto.JolpicaDriver;
import com.fantasyformula.backend.integration.jolpica.dto.JolpicaStandingsResponse.DriverStanding;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverPricingServiceTest {

    @Test
    void calculatesPricesWithinConfiguredLimits() throws Exception {

        // Create a mock client so this test does not call the live API.
        JolpicaClient client = mock(JolpicaClient.class);

        // Sample 2025 championship standings.
        List<DriverStanding> standings2025 = List.of(
                standing("norris", "423", "4", "NOR", "Lando", "Norris"),
                standing("max_verstappen", "421", "1", "VER", "Max", "Verstappen"),
                standing("piastri", "410", "81", "PIA", "Oscar", "Piastri"),
                standing("russell", "319", "63", "RUS", "George", "Russell"),
                standing("leclerc", "242", "16", "LEC", "Charles", "Leclerc"),
                standing("hamilton", "156", "44", "HAM", "Lewis", "Hamilton"),
                standing("antonelli", "150", "12", "ANT", "Kimi", "Antonelli"),
                standing("albon", "73", "23", "ALB", "Alexander", "Albon")
        );

        when(client.getFinalDriverStandings(2025))
                .thenReturn(standings2025);

        // Sample previous-season results for the fallback calculation.
        when(client.getFinalDriverStandings(2024))
                .thenReturn(List.of(
                        standing("perez", "152", "11", "PER",
                                "Sergio", "Perez"),
                        standing("bottas", "49", "77", "BOT",
                                "Valtteri", "Bottas")
                ));

        // Load the real configuration so the service uses the configured rookie list.
        ObjectMapper objectMapper = new ObjectMapper();
        Resource configFile = new ClassPathResource("fantasy-drivers-2026.json");

        FantasyDriverEligibilityService eligibilityService =
                new FantasyDriverEligibilityService(objectMapper, configFile);

        // Use the updated two-argument constructor.
        DriverPricingService service =
                new DriverPricingService(client, eligibilityService);

        // Sample salary estimates, expressed in millions.
        Map<String, BigDecimal> salaries = Map.of(
                "max_verstappen", new BigDecimal("70"),
                "norris", new BigDecimal("30"),
                "piastri", new BigDecimal("13"),
                "russell", new BigDecimal("34"),
                "leclerc", new BigDecimal("34"),
                "hamilton", new BigDecimal("60"),
                "antonelli", new BigDecimal("2"),
                "albon", new BigDecimal("12")
        );

        // Use configured eligible drivers and add one configured test-independent
        // driver ID only if it is part of the real eligibility list.
        Set<String> eligibleDriverIds = new HashSet<>(salaries.keySet());
        eligibleDriverIds.addAll(eligibilityService.getEligibleDriverIds());

        Map<String, BigDecimal> prices = service.calculateInitialPrices(
                2025,
                eligibleDriverIds,
                salaries
        );

        // Every requested driver should receive a price.
        assertEquals(eligibleDriverIds.size(), prices.size());

        // Verify the minimum and maximum price constraints.
        prices.values().forEach(price -> {
            assertNotNull(price);
            assertTrue(
                    price.compareTo(new BigDecimal("4.00")) >= 0,
                    "Driver price must be at least $4M: " + price
            );
            assertTrue(
                    price.compareTo(new BigDecimal("40.00")) <= 0,
                    "Driver price must not exceed $40M: " + price
            );
        });

        // At least five drivers must fit within the team budget.
        BigDecimal cheapestFiveTotal = prices.values().stream()
                .sorted()
                .limit(5)
                .reduce(BigDecimal.ZERO, (total, price) -> total.add(price));

        assertTrue(
                cheapestFiveTotal.compareTo(new BigDecimal("200.00")) <= 0,
                "The five cheapest drivers should fit within the $200M budget"
        );

        // Confirm that both seasons were requested.
        verify(client).getFinalDriverStandings(2025);
        verify(client).getFinalDriverStandings(2024);
    }

    @Test
    void pricesAllConfigured2026DriversWithinBudgetRules() throws Exception {

        // Load the actual 2026 eligibility and rookie configuration.
        ObjectMapper objectMapper = new ObjectMapper();
        Resource configFile = new ClassPathResource("fantasy-drivers-2026.json");

        FantasyDriverEligibilityService eligibilityService =
                new FantasyDriverEligibilityService(objectMapper, configFile);

        Set<String> eligibleDriverIds =
                eligibilityService.getEligibleDriverIds();

        assertFalse(eligibleDriverIds.isEmpty());

        // Mock the Jolpica API client.
        JolpicaClient client = mock(JolpicaClient.class);

        // Provide sample 2025 results.
        List<DriverStanding> standings2025 = List.of(
                standing("norris", "423", "4", "NOR", "Lando", "Norris"),
                standing("max_verstappen", "421", "1", "VER", "Max", "Verstappen"),
                standing("piastri", "410", "81", "PIA", "Oscar", "Piastri"),
                standing("russell", "319", "63", "RUS", "George", "Russell"),
                standing("leclerc", "242", "16", "LEC", "Charles", "Leclerc"),
                standing("hamilton", "156", "44", "HAM", "Lewis", "Hamilton"),
                standing("albon", "73", "23", "ALB", "Alexander", "Albon")
        );

        when(client.getFinalDriverStandings(2025))
                .thenReturn(standings2025);

        // Provide sample 2024 results for the fallback.
        when(client.getFinalDriverStandings(2024))
                .thenReturn(List.of(
                        standing("perez", "152", "11", "PER",
                                "Sergio", "Perez"),
                        standing("bottas", "49", "77", "BOT",
                                "Valtteri", "Bottas")
                ));

        // Use the updated constructor with both required dependencies.
        DriverPricingService pricingService =
                new DriverPricingService(client, eligibilityService);

        // No salary estimates are supplied, so the service uses its neutral salary score.
        Map<String, BigDecimal> prices = pricingService.calculateInitialPrices(
                2025,
                eligibleDriverIds,
                Map.of()
        );

        assertEquals(eligibleDriverIds.size(), prices.size());

        // Check every price is within the configured limits.
        prices.forEach((driverId, price) -> {
            assertNotNull(price, "Missing price for " + driverId);
            assertTrue(
                    price.compareTo(new BigDecimal("4.00")) >= 0
                            && price.compareTo(new BigDecimal("40.00")) <= 0,
                    "Price outside $4M–$40M for " + driverId + ": " + price
            );
        });

        // Check that at least five drivers exist and the cheapest five fit the budget.
        assertTrue(eligibleDriverIds.size() >= 5);

        BigDecimal cheapestFiveTotal = prices.values().stream()
                .sorted()
                .limit(5)
                .reduce(BigDecimal.ZERO, (total, price) -> total.add(price));

        assertTrue(
                cheapestFiveTotal.compareTo(new BigDecimal("200.00")) <= 0,
                "The five cheapest drivers must fit within the $200M budget"
        );

        // System.out.println("Eligible 2026 drivers: " + eligibleDriverIds.size());
        // System.out.println("Prices calculated: " + prices.size());
        // System.out.println("Five cheapest drivers total: $" + cheapestFiveTotal + "M");
        // System.out.println("All calculated prices: " + prices);

        // Confirm the expected grid size and key drivers.
        assertEquals(22, eligibleDriverIds.size());
        assertEquals(22, prices.size());
        assertTrue(prices.containsKey("bottas"));
        assertTrue(prices.containsKey("perez"));
    }

    // Helper method to construct sample championship standings.
    private DriverStanding standing(
            String id,
            String points,
            String number,
            String code,
            String firstName,
            String lastName) {

        JolpicaDriver driver = new JolpicaDriver(
                id,
                number,
                code,
                firstName,
                lastName,
                "Unknown"
        );

        return new DriverStanding("1", points, "0", driver);
    }
}
