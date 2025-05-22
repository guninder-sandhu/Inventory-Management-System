package com.inventory.dashboardservice.controller;

import com.inventory.dashboardservice.apiresponse.ApiResponse;
import com.inventory.dashboardservice.dto.DashboardDto;
import com.inventory.dashboardservice.impl.DashboardServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardServiceImpl service;


    public DashboardController(DashboardServiceImpl service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<DashboardDto>> prepareDashboard(
            @RequestParam(name = "lowStockThreshold", defaultValue = "5")int lowStockThreshold
            ) {
        ApiResponse<DashboardDto> response = new ApiResponse<>(
                "Dashboard prepared successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.prepareDashboardDto(lowStockThreshold));
        log.info("Dashboard prepared successfully {}",response.getData());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
