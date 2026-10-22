package com.fantasyformula.backend.integration.jolpica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JolpicaMetadata(
        @JsonProperty("DriverTable") JolpicaDriverTable driverTable
) {
}
