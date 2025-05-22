package com.inventory.dashboardservice.impl;

import com.inventory.dashboardservice.apiresponse.ApiResponse;
import com.inventory.dashboardservice.dto.DashboardDto;
import com.inventory.dashboardservice.externalapicalls.client.ProductClient;
import com.inventory.dashboardservice.externalapicalls.client.StockClient;
import com.inventory.dashboardservice.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    private final ProductClient productClient;
    private final StockClient stockClient;

    public DashboardServiceImpl(ProductClient productClient, StockClient stockClient) {
        this.productClient = productClient;
        this.stockClient = stockClient;
    }

    @Override
    public DashboardDto prepareDashboardDto(int lowStockThreshold) {
        log.info("Preparing Dashboard");
        DashboardDto dashboardDto = new DashboardDto();
        retrieveProductCount(dashboardDto);
        retrieveStockCount(dashboardDto);
        retrieveTotalInventoryCost(dashboardDto);
        retrieveLowStockCount(dashboardDto,lowStockThreshold);
        return dashboardDto;
    }

    private void retrieveProductCount(DashboardDto dashboardDto) {
        var productCountResponse = productClient.getProductCount();
        if (productCountResponse == null || productCountResponse.getData() == null) {
            log.error("Unable to retrieve product count from product service");
            dashboardDto.setProductCount(-1);

        } else {
            dashboardDto.setProductCount(productCountResponse.getData());
        }
    }

    private void retrieveStockCount(DashboardDto dashboardDto) {
        var stockCountResponse = stockClient.getStockCount();
        if (stockCountResponse == null || stockCountResponse.getData() == null) {
            log.error("Unable to retrieve stock count from stock service");
            dashboardDto.setStockCount(-1);

        } else {
            dashboardDto.setStockCount(stockCountResponse.getData());
        }
    }

    private void retrieveTotalInventoryCost(DashboardDto dashboardDto) {
        var inventoryCostResponse = productClient.getTotalInventoryCost();
        if (inventoryCostResponse == null || inventoryCostResponse.getData() == null) {
            log.error("Unable to retrieve inventory cost from stock service");
            dashboardDto.setStockCount(-1);

        } else {
            dashboardDto.setTotalInventoryCost(inventoryCostResponse.getData());
        }
    }

    private void retrieveLowStockCount(DashboardDto dashboardDto, int lowStockThreshold) {
        ApiResponse<Integer> lowStockProductsCount = productClient.getLowStockProductsCount(lowStockThreshold);

        if (lowStockProductsCount == null || lowStockProductsCount.getData() == null) {
            log.error("Unable to retrieve low product  count from product service");
            dashboardDto.setLowStockCount(-1);

        } else {
            dashboardDto.setLowStockCount(lowStockProductsCount.getData());
        }
    }
}
