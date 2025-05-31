package com.inventory.orderservice.externalapiclient.clients;

import com.inventory.orderservice.dto.ProductRetrievedDto;
import com.inventory.orderservice.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("product/code/{productCode}")
    ResponseEntity<ApiResponse<ProductRetrievedDto>> getProductByCode(@PathVariable("productCode") String productCode);
}
