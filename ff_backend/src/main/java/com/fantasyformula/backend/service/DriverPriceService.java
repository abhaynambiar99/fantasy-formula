
package com.fantasyformula.backend.service;

import com.fantasyformula.backend.entity.DriverPrice;
import com.fantasyformula.backend.repository.DriverPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DriverPriceService {

    private final DriverPriceRepository driverPriceRepository;

    public DriverPriceService(DriverPriceRepository driverPriceRepository) {
        this.driverPriceRepository = driverPriceRepository;
    }

    public List<DriverPrice> getPricesBySeason(Integer seasonYear) {
        if (seasonYear == null || seasonYear < 1950 || seasonYear > 2100) {
            throw new IllegalArgumentException("Invalid season year");
        }

        return driverPriceRepository
                .findByIdSeasonYearOrderByCurrentPriceDesc(seasonYear);
    }
}
