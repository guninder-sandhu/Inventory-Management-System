package com.inventory.orderservice.controller;

import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.entities.PurchaseOrderItems;
import com.inventory.orderservice.response.ApiResponse;
import com.inventory.orderservice.services.PurchaseOrderItemService;
import com.inventory.orderservice.services.PurchaseOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/purchase-item")
public class PurchaseOrderItemController {

    private final PurchaseOrderItemService service;
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderItemController(PurchaseOrderItemService purchaseOrderItemService, PurchaseOrderService purchaseOrderService) {
        this.service = purchaseOrderItemService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/create/{orderId}")
    public ResponseEntity<ApiResponse<PurchaseOrderItems>> createPurchaseOrderItems(
            @RequestBody PurchaseOrderItemDto purchaseOrderItemsDto, @PathVariable String orderId) {
        var purchaseOrder = purchaseOrderService.findPurchaseOrderById(orderId);
        ApiResponse<PurchaseOrderItems> response = new ApiResponse<>(
                "Purchase order item created successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.createPurchaseOrderItem(purchaseOrderItemsDto, purchaseOrder));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update-item/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrderItems>> updatePurchaseOrderItems(@PathVariable String id,
                                                                                    @RequestBody PurchaseOrderItemDto purchaseOrderItemsDto) {
        ApiResponse<PurchaseOrderItems> response = new ApiResponse<>(
                "Purchase order item updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.updatePurchaseOrderItem(id, purchaseOrderItemsDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrderItems>> getPurchaseOrderItemById(@PathVariable String id) {
        ApiResponse<PurchaseOrderItems> response = new ApiResponse<>(
                "Purchase order item retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findPurchaseOrderItemById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<List<PurchaseOrderItems>>> getPurchaseOrderItemByOrderId(@PathVariable String orderId) {
        ApiResponse<List<PurchaseOrderItems>> response = new ApiResponse<>(
                "All Purchase order item retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findPurchaseOrderItemsByPurchaseOrder(orderId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<PurchaseOrderItems>>> getAllPurchaseOrderItem() {
        ApiResponse<List<PurchaseOrderItems>> response = new ApiResponse<>(
                "All Purchase order item retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findAllItems());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/delete/{poid}")
    public ResponseEntity<ApiResponse<PurchaseOrderItems>> deletePurchaseOrderItems(
            @PathVariable String poid) {
        service.deletePurchaseOrderItemById(poid);
        ApiResponse<PurchaseOrderItems> response = new ApiResponse<>(
                "Purchase order item deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllCreatedOn/{createdOn}")
    public ResponseEntity<ApiResponse<List<PurchaseOrderItems>>> getAllItemsCreatedOn(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdOn) {

        ApiResponse<List<PurchaseOrderItems>> response = new ApiResponse<>(
                "Purchase order items fetched successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findAllItemsCreatedOn(createdOn));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
