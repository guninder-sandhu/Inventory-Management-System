package com.inventory.orderservice.controller;

import com.inventory.orderservice.dto.VendorDto;
import com.inventory.orderservice.entities.Vendor;
import com.inventory.orderservice.response.ApiResponse;
import com.inventory.orderservice.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Vendor>> updateVendor(@RequestBody Vendor vendor) {
        var updatedVendor = service.updateVendor(vendor);
        if (updatedVendor == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Unable to save vendor", 400, LocalDateTime.now(), null)
            );
        }

        ApiResponse<Vendor> response = new ApiResponse<>(
                "Vendor already exist",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                updatedVendor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{vendorCode}")
    public ResponseEntity<ApiResponse<Vendor>> deleteVendor(@PathVariable String vendorCode) {
        service.deleteVendor(vendorCode);
        ApiResponse<Vendor> response = new ApiResponse<>(
                "Vendor deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<Vendor>>> getAllVendors() {
        ApiResponse<List<Vendor>> response = new ApiResponse<>(
                "Vendors retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findVendors());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getVendor/byCode/{code}")
    public ResponseEntity<ApiResponse<Vendor>> getVendorByCode(
            @PathVariable String code
    ) {
        ApiResponse<Vendor> response = new ApiResponse<>(
                "Vendor retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findVendorsByVendorCode(code));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getVendor/byId/{id}")
    public ResponseEntity<ApiResponse<Vendor>> getVendorById(
            @PathVariable String id
    ) {
        ApiResponse<Vendor> response = new ApiResponse<>(
                "Vendor retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findVendorByVendorId(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getVendor/byName/{name}")
    public ResponseEntity<ApiResponse<Vendor>> getVendorByName(
            @PathVariable String name
    ) {
        ApiResponse<Vendor> response = new ApiResponse<>(
                "Vendor retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.findVendorsByVendorName(name));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}