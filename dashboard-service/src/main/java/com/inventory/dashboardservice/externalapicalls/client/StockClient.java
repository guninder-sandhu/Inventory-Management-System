package com.inventory.dashboardservice.externalapicalls.client;

import com.inventory.dashboardservice.apiresponse.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "STOCK-SERVICE")
public interface StockClient {

    @GetMapping("/stocks/total")
    ApiResponse<Integer> getStockCount();
}
