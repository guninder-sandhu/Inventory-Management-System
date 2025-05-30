package com.inventory.orderservice.controller;

import com.inventory.orderservice.dto.VendorDto;
import com.inventory.orderservice.entities.Vendor;
import com.inventory.orderservice.response.ApiResponse;
import com.inventory.orderservice.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("vendor")
public class VendorController {

    private final VendorService service;

    public VendorController(VendorService vendorService) {
        this.service = vendorService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Vendor>> createVendor(@RequestBody VendorDto vendorDto) {
        var vendorCreationResult = service.createVendor(vendorDto);
        if (vendorCreationResult == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Unable to create vendor", 400, LocalDateTime.now(), null)
            );
        }
        if (vendorCreationResult.isDuplicate()) {
            ApiResponse<Vendor> response = new ApiResponse<>(
                    "Vendor already exist",
                    HttpStatus.OK.value(),
                    LocalDateTime.now(),
                    vendorCreationResult.getVendor());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponse<Vendor> response = new ApiResponse<>(
                "Vendor created successfully",
                HttpStatus.CREATED.value(),
                LocalDateTime.now(),
                vendorCreationResult.getVendor());
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
