
package com.fantasyformula.backend.controller;

import com.fantasyformula.backend.dto.DriverPriceResponse;
import com.fantasyformula.backend.entity.DriverPrice;
import com.fantasyformula.backend.service.DriverPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drivers/prices")
public class DriverPriceController {

    private final DriverPriceService driverPriceService;

    public DriverPriceController(DriverPriceService driverPriceService) {
        this.driverPriceService = driverPriceService;
    }

    @GetMapping
    public List<DriverPriceResponse> getDriverPrices(
            @RequestParam Integer season) {

        List<DriverPrice> prices =
                driverPriceService.getPricesBySeason(season);

        return prices.stream()
                .map(DriverPriceResponse::from)
                .toList();
    }
}
