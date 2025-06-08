package com.inventory.orderservice.services;

import com.inventory.orderservice.dto.VendorDto;
import com.inventory.orderservice.entities.Vendor;
import com.inventory.orderservice.response.VendorCreationResult;

import java.util.List;

public interface VendorService {
    VendorCreationResult createVendor(VendorDto vendorDto);

    Vendor updateVendor(Vendor vendor);

    void deleteVendor(String id);

    List<Vendor> findVendors();

    Vendor findVendorByVendorId(String id);

    Vendor findVendorsByVendorName(String vendorName);

    Vendor findVendorsByVendorCode(String vendorCode);

}
