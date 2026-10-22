
package com.fantasyformula.backend.integration.jolpica;

import com.fantasyformula.backend.integration.jolpica.dto.JolpicaDriver;
import com.fantasyformula.backend.integration.jolpica.dto.JolpicaResponse;
//import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.fantasyformula.backend.integration.jolpica.dto.JolpicaStandingsResponse;

import java.util.List;

@Component
public class JolpicaClient {

    private final RestClient f1RestClient;

    public JolpicaClient(RestClient f1RestClient) {
        this.f1RestClient = f1RestClient;
    }

    public List<JolpicaDriver> getDriversBySeason(int season) {
        JolpicaResponse response = f1RestClient.get()
                .uri("/{season}/drivers.json?limit=100", season)
                .header("User-Agent", "FantasyFormula/0.1.0")
                .retrieve()
                .body(JolpicaResponse.class);

        if (response == null
                || response.metadata() == null
                || response.metadata().driverTable() == null
                || response.metadata().driverTable().drivers() == null) {
            throw new IllegalStateException(
                    "Jolpica returned an unexpected driver response");
        }

        return response.metadata().driverTable().drivers();
    }

    public List<JolpicaStandingsResponse.DriverStanding> getFinalDriverStandings(int season) {

        JolpicaStandingsResponse response = f1RestClient.get()
                .uri("/{season}/driverStandings.json?limit=100", season)
                .header("User-Agent", "FantasyFormula/0.1.0")
                .retrieve()
                .body(JolpicaStandingsResponse.class);

        if (response == null
                || response.data() == null
                || response.data().standingsTable() == null
                || response.data().standingsTable().standingsLists() == null
                || response.data().standingsTable().standingsLists().isEmpty()
                || response.data().standingsTable().standingsLists()
                        .getFirst().driverStandings() == null) {
            throw new IllegalStateException(
                    "Jolpica returned an unexpected standings response");
        }

        return response.data().standingsTable()
                .standingsLists()
                .getFirst()
                .driverStandings();
    }

}
