
package com.fantasyformula.backend.integration.jolpica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JolpicaDriverTable(
        String season,
        @JsonProperty("Drivers") List<JolpicaDriver> drivers
) {
}
