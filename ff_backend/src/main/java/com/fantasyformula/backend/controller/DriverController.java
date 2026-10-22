
package com.fantasyformula.backend.controller;

import com.fantasyformula.backend.dto.DriverCatalogueResponse;
import com.fantasyformula.backend.service.DriverService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<DriverCatalogueResponse> getDrivers(
            @RequestParam Integer season) {
        return driverService.getDriversBySeason(season);
    }
}
