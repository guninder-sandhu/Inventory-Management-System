package com.inventory.orderservice.controller;

import com.inventory.orderservice.dto.PurchaseOrderRequestDto;
import com.inventory.orderservice.entities.PurchaseOrder;
import com.inventory.orderservice.response.ApiResponse;
import com.inventory.orderservice.services.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderRequestController {

    private final PurchaseOrderService service;

    public PurchaseOrderRequestController(PurchaseOrderService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PurchaseOrder>> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDto purchaseOrderRequest) {
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Purchase Order created successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.createPurchaseOrder(purchaseOrderRequest));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PurchaseOrder>> deleteOrder(@RequestBody PurchaseOrder purchaseOrder) {
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.updatePurchaseOrder(purchaseOrder));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete-by-id/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> deleteOrderById(@PathVariable String id) {
        service.deletePurchaseOrder(id);
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{code}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> deleteOrder(@PathVariable String code) {
        service.deletePurchaseOrderByCode(code);
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<PurchaseOrder>> deletePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        service.deletePurchaseOrder(purchaseOrder);
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/delete-bulk")
    public ResponseEntity<ApiResponse<PurchaseOrder>> deletePurchaseOrderBulk(@RequestBody List<String> orderIds) {
        service.deletePurchaseOrders(orderIds);
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search-by-vendor/{vendorId}")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> searchByVendorId(@PathVariable String vendorId) {
        ApiResponse<List<PurchaseOrder>> response = new ApiResponse<>(
                "Order retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.findPurchaseOrdersByVendorId(vendorId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search/{orderId}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> searchByOrderId(@PathVariable String orderId) {
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.findPurchaseOrderById(orderId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search-by-order-code/{orderCode}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> searchByOrderCode(@PathVariable String orderCode) {
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.findPurchaseOrderByCode(orderCode));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search-by-vendor-code/{vendorCode}")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> searchByVendorCode(@PathVariable String vendorCode) {
        ApiResponse<List<PurchaseOrder>> response = new ApiResponse<>(
                "Order retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.findPurchaseOrderByVendorCode(vendorCode));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/search-by-ids")
    public ResponseEntity<ApiResponse<PurchaseOrder>> searchByOrderIdAndVendorId(@RequestParam String orderId,
                                                                                 @RequestParam String vendorId) {
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.findPurchaseOrderByIdAndVendorId(orderId, vendorId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search-by-codes")
    public ResponseEntity<ApiResponse<PurchaseOrder>> searchByOrderCodeAndVendorCode(@RequestParam String orderCode,
                                                                                     @RequestParam String vendorCode) {
        ApiResponse<PurchaseOrder> response = new ApiResponse<>(
                "Order retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), service.findPurchaseOrderByCodeAndVendorCode(orderCode, vendorCode));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
