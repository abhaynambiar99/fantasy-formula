
package com.fantasyformula.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DriverPriceId implements Serializable {

    @Column(name = "season_year")
    private Integer seasonYear;

    @Column(name = "driver_external_id")
    private String driverExternalId;

    public DriverPriceId() {
    }

    public DriverPriceId(Integer seasonYear, String driverExternalId) {
        this.seasonYear = seasonYear;
        this.driverExternalId = driverExternalId;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public String getDriverExternalId() {
        return driverExternalId;
    }

    public void setDriverExternalId(String driverExternalId) {
        this.driverExternalId = driverExternalId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof DriverPriceId that)) return false;

        return Objects.equals(seasonYear, that.seasonYear)
                && Objects.equals(driverExternalId, that.driverExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonYear, driverExternalId);
    }
}
