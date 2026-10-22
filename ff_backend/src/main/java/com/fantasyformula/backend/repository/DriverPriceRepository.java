
package com.fantasyformula.backend.repository;

import com.fantasyformula.backend.entity.DriverPrice;
import com.fantasyformula.backend.entity.DriverPriceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverPriceRepository
        extends JpaRepository<DriverPrice, DriverPriceId> {

    List<DriverPrice> findByIdSeasonYearOrderByCurrentPriceDesc(
            Integer seasonYear
    );
}
