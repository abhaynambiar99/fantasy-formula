
package com.fantasyformula.backend.service;

import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class FantasyDriverEligibilityService {

    // IDs of drivers eligible for the fantasy game.
    private final Set<String> eligibleDriverIds;

    // IDs explicitly classified as rookies for this season.
    private final Set<String> rookieDriverIds;

    public FantasyDriverEligibilityService(
            ObjectMapper objectMapper,
            @Value("classpath:fantasy-drivers-2026.json")
            Resource configFile) throws IOException {

        // Read the season's driver configuration from the JSON file.
        SeasonDriverConfig config = objectMapper.readValue(
                configFile.getInputStream(),
                SeasonDriverConfig.class);

        // Copy the configured IDs into sets for efficient lookups.
        this.eligibleDriverIds = new HashSet<>(config.driverIds());

        // A missing rookieDriverIds property is treated as an empty list.
        this.rookieDriverIds = config.rookieDriverIds() == null
                ? new HashSet<>()
                : new HashSet<>(config.rookieDriverIds());

        // Every configured rookie must also be eligible to play.
        if (!eligibleDriverIds.containsAll(rookieDriverIds)) {
            throw new IllegalStateException(
                    "Every rookie driver must be in the eligible driver list");
        }
    }

    // Check whether a driver can appear in the fantasy game.
    public boolean isEligible(String driverId) {
        return eligibleDriverIds.contains(driverId);
    }

    // Return an immutable copy of the eligible driver IDs.
    public Set<String> getEligibleDriverIds() {
        return Set.copyOf(eligibleDriverIds);
    }

    // Check whether a driver is explicitly classified as a rookie.
    public boolean isRookie(String driverId) {
        return rookieDriverIds.contains(driverId);
    }

    // Return an immutable copy of the rookie driver IDs.
    public Set<String> getRookieDriverIds() {
        return Set.copyOf(rookieDriverIds);
    }

    // Java record representing the fields in the JSON configuration.
    private record SeasonDriverConfig(
            int season,
            Set<String> driverIds,
            Set<String> rookieDriverIds) {
    }
}
