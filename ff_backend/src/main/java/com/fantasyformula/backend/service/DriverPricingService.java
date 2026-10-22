
package com.fantasyformula.backend.service;

import com.fantasyformula.backend.integration.jolpica.JolpicaClient;
import com.fantasyformula.backend.integration.jolpica.dto.JolpicaStandingsResponse.DriverStanding;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DriverPricingService {

        // Minimum and maximum permitted driver prices, in millions.
        private static final BigDecimal MIN_PRICE = new BigDecimal("4.00");
        private static final BigDecimal MAX_PRICE = new BigDecimal("40.00");

        // Performance has more influence than estimated salary.
        private static final double PERFORMANCE_WEIGHT = 0.75;
        private static final double SALARY_WEIGHT = 0.25;

        // Explicit fallback for drivers with no positive points in either season.
        // This is a configurable product decision, not an official F1 Fantasy rule.
        // Fallback performance scores when neither season has usable points.
        private static final double ESTABLISHED_DRIVER_FALLBACK_SCORE = 0.40;
        private static final double ROOKIE_FALLBACK_SCORE = 0.25;

        private final JolpicaClient jolpicaClient;
        private final FantasyDriverEligibilityService eligibilityService;

        public DriverPricingService(
                        JolpicaClient jolpicaClient,
                        FantasyDriverEligibilityService eligibilityService) {

                this.jolpicaClient = jolpicaClient;
                this.eligibilityService = eligibilityService;
        }

        public Map<String, BigDecimal> calculateInitialPrices(
                        int historicalSeason,
                        Set<String> eligibleDriverIds,
                        Map<String, BigDecimal> annualSalaries) {

                // Validate inputs before making API calls.
                if (eligibleDriverIds == null || eligibleDriverIds.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Eligible driver IDs cannot be empty");
                }

                if (annualSalaries == null) {
                        throw new IllegalArgumentException(
                                        "Annual salaries cannot be null");
                }

                // Retrieve final standings for the latest completed season.
                List<DriverStanding> currentStandings = jolpicaClient.getFinalDriverStandings(historicalSeason);

                // Retrieve the preceding season for drivers missing from the latest one.
                List<DriverStanding> previousStandings = jolpicaClient.getFinalDriverStandings(historicalSeason - 1);

                // Convert each season's points into a normalized performance score.
                // A score of 1.0 means the driver had the most points in that season.
                Map<String, Double> currentScores = normalizePerformance(currentStandings);

                Map<String, Double> previousScores = normalizePerformance(previousStandings);

                // Find the lowest and highest available non-negative salary estimates.
                BigDecimal minimumSalary = annualSalaries.values().stream()
                                .filter(salary -> salary != null && salary.signum() >= 0)
                                .min(Comparator.naturalOrder())
                                .orElse(BigDecimal.ZERO);

                BigDecimal maximumSalary = annualSalaries.values().stream()
                                .filter(salary -> salary != null && salary.signum() >= 0)
                                .max(Comparator.naturalOrder())
                                .orElse(BigDecimal.ZERO);

                // Calculate one initial price for each eligible driver.
                Map<String, BigDecimal> prices = new LinkedHashMap<>();

                for (String driverId : eligibleDriverIds) {

                        // Prefer the latest season's performance when available.
                        // Otherwise, fall back to the preceding season.
                        double performanceScore;

                        if (currentScores.containsKey(driverId)) {
                                performanceScore = currentScores.get(driverId);
                        } else if (previousScores.containsKey(driverId)) {
                                performanceScore = previousScores.get(driverId);
                        } else if (eligibilityService.isRookie(driverId)) {
                                // True rookies receive the lower fallback performance score.
                                performanceScore = ROOKIE_FALLBACK_SCORE;
                        } else {
                                // Established drivers without usable recent points receive a higher baseline.
                                performanceScore = ESTABLISHED_DRIVER_FALLBACK_SCORE;
                        }

                        // System.out.println(
                        //                 driverId
                        //                                 + " | current season score: " + currentScores.get(driverId)
                        //                                 + " | previous season score: " + previousScores.get(driverId)
                        //                                 + " | rookie: " + eligibilityService.isRookie(driverId)
                        //                                 + " | selected score: " + performanceScore);

                        // Calculate a price using performance and available salary data.
                        BigDecimal price = calculatePrice(
                                        performanceScore,
                                        annualSalaries.get(driverId),
                                        minimumSalary,
                                        maximumSalary);

                        prices.put(driverId, price);
                }

                return prices;
        }

        private Map<String, Double> normalizePerformance(
                        List<DriverStanding> standings) {

                // An empty response should not cause a null-pointer error.
                if (standings == null || standings.isEmpty()) {
                        return Map.of();
                }

                // Convert championship points from text to integers.
                Map<String, Integer> pointsByDriver = standings.stream()
                                .collect(Collectors.toMap(
                                                standing -> standing.driver().driverId(),
                                                this::getPoints,
                                                (existingPoints, incomingPoints) -> existingPoints
                                                                .compareTo(incomingPoints) >= 0
                                                                                ? existingPoints
                                                                                : incomingPoints));

                // Find the maximum points scored in this particular season.
                int maximumPoints = pointsByDriver.values().stream()
                                .mapToInt(points -> points == null ? 0 : points)
                                .max()
                                .orElse(0);

                // No positive points means there is no useful performance baseline.
                if (maximumPoints <= 0) {
                        return Map.of();
                }

                // Normalize each driver's points against that season's maximum.
                Map<String, Double> normalizedScores = new LinkedHashMap<>();

                pointsByDriver.forEach((driverId, points) -> normalizedScores.put(
                                driverId,
                                (double) points / maximumPoints));

                return normalizedScores;
        }

        private BigDecimal calculatePrice(
                        double performanceScore,
                        BigDecimal annualSalary,
                        BigDecimal minimumSalary,
                        BigDecimal maximumSalary) {

                // Missing salary estimates use a neutral score instead of an invented salary.
                double salaryScore = 0.5;

                // Normalize salary only when usable salary data has a range.
                if (annualSalary != null
                                && annualSalary.signum() >= 0
                                && maximumSalary.compareTo(minimumSalary) > 0) {

                        salaryScore = annualSalary.subtract(minimumSalary).doubleValue()
                                        / maximumSalary.subtract(minimumSalary).doubleValue();
                }

                // Combine performance and salary using the agreed weights.
                double combinedScore = PERFORMANCE_WEIGHT * performanceScore
                                + SALARY_WEIGHT * salaryScore;

                // Map the combined score into the configured price range.
                double price = MIN_PRICE.doubleValue()
                                + (MAX_PRICE.doubleValue() - MIN_PRICE.doubleValue())
                                                * combinedScore;

                // Guard against rounding or calculation values outside the limits.
                price = Math.max(
                                MIN_PRICE.doubleValue(),
                                Math.min(MAX_PRICE.doubleValue(), price));

                // Keep prices to two decimal places.
                return BigDecimal.valueOf(price)
                                .setScale(2, RoundingMode.HALF_UP);
        }

        private int getPoints(DriverStanding standing) {
                // Jolpica provides championship points as a string.
                return Integer.parseInt(standing.points());
        }
}
