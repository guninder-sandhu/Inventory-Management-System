package com.inventory.dashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    Integer productCount;
    Integer stockCount;
    Double totalInventoryCost;
    Integer lowStockCount;
}
