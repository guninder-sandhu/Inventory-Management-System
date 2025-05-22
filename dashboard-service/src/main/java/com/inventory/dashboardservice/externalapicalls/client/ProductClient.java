package com.inventory.dashboardservice.externalapicalls.client;

import com.inventory.dashboardservice.apiresponse.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/product/count")
    ApiResponse<Integer> getProductCount();

    @GetMapping("/product/totalValue")
    ApiResponse<Double> getTotalInventoryCost();

    @GetMapping("/product/lowStockCount")
    ApiResponse<Integer> getLowStockProductsCount(@RequestParam(name = "threshold", required = false, defaultValue = "5") int threshold);
}
