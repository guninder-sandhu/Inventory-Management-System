package com.inventory.dashboardservice.service;

import com.inventory.dashboardservice.dto.DashboardDto;

public interface DashboardService {

    DashboardDto prepareDashboardDto(int lowStockThreshold);
}
