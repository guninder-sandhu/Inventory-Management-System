package com.inventory.orderservice.response;

import com.inventory.orderservice.entities.Vendor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VendorCreationResult {
    private Vendor vendor;
    private boolean isDuplicate;

}

