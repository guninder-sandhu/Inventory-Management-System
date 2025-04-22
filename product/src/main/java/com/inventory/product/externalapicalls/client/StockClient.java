package com.inventory.product.externalapicalls.client;

import com.inventory.product.dto.StockDto;
import com.inventory.product.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "STOCK-SERVICE")
public interface StockClient {

    @GetMapping("/stocks/{productCode}")
    ApiResponse<StockDto> getProductQuantityFromCode(@PathVariable("productCode") String productCode);

    @PostMapping("/stocks")
    ApiResponse<StockDto> createStock(StockDto stockDto);
}
