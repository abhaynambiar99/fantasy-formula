
package com.fantasyformula.backend.integration.jolpica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JolpicaResponse(
        @JsonProperty("MRData") JolpicaMetadata metadata
) {
}
