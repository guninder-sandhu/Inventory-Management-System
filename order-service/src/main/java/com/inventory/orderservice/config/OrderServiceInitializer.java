package com.inventory.orderservice.config;

import com.inventory.orderservice.entities.PurchaseOrderCount;
import com.inventory.orderservice.entities.VendorCount;
import com.inventory.orderservice.repository.PurchaseOrderCountRepository;
import com.inventory.orderservice.repository.VendorCountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceInitializer {

    @Autowired
    private VendorCountRepository vendorCountRepository;

    @Autowired
    private PurchaseOrderCountRepository purchaseOrderCountRepository;

    @PostConstruct
    public void initCounts() {
        // Initialize VendorCount if table is empty
        if (vendorCountRepository.count() == 0) {
            VendorCount vendorCount = new VendorCount();
            vendorCount.setId(1);
            vendorCount.setVendorCount(0);
            vendorCountRepository.save(vendorCount);
        }

        // Initialize PurchaseOrderCount if table is empty
        if (purchaseOrderCountRepository.count() == 0) {
            PurchaseOrderCount poCount = new PurchaseOrderCount();
            poCount.setId(1);
            poCount.setPurchaseOrderCount(0);
            purchaseOrderCountRepository.save(poCount);
        }
    }
}
