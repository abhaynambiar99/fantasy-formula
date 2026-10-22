package com.fantasyformula.backend.integration.jolpica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JolpicaDriver(
                String driverId,
                String permanentNumber,
                String code,
                String givenName,
                String familyName,
                String nationality) {
}
