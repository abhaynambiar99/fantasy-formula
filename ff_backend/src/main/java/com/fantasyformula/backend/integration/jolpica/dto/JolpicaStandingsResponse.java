
package com.fantasyformula.backend.integration.jolpica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JolpicaStandingsResponse(
        @JsonProperty("MRData") Data data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            @JsonProperty("StandingsTable")
            StandingsTable standingsTable
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StandingsTable(
            @JsonProperty("StandingsLists")
            List<StandingsList> standingsLists
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StandingsList(
            String season,
            String round,
            @JsonProperty("DriverStandings")
            List<DriverStanding> driverStandings
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DriverStanding(
            String position,
            String points,
            String wins,
            @JsonProperty("Driver")
            JolpicaDriver driver
    ) {}
}
