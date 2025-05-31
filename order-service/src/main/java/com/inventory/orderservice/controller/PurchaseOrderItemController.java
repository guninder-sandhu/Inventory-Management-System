package com.inventory.orderservice.controller;

import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.entities.PurchaseOrderItems;
import com.inventory.orderservice.response.ApiResponse;
import com.inventory.orderservice.services.PurchaseOrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/purchase-item")
public class PurchaseOrderItemController {

    private final PurchaseOrderItemService service;

    public PurchaseOrderItemController(PurchaseOrderItemService purchaseOrderItemService) {
        this.service = purchaseOrderItemService;
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PurchaseOrderItems>> createPurchaseOrderItems(
            @RequestBody PurchaseOrderItemDto purchaseOrderItemsDto) {
        ApiResponse<PurchaseOrderItems> response = new ApiResponse<>(
                "Purchase order item created successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.createPurchaseOrderItem(purchaseOrderItemsDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
