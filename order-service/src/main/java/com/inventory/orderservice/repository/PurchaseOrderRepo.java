package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrder, String> {

    void deleteByOrderCode(String purchaseOrderCode);

    List<PurchaseOrder> findByVendor_VendorId(String id);

    PurchaseOrder findByOrderIdAndVendor_VendorId(String orderId, String vendorId);

    PurchaseOrder findByOrderCodeAndVendor_VendorCode(String orderId, String vendorId);

    PurchaseOrder findByOrderCode(String orderCode);

    List<PurchaseOrder> findByVendor_VendorCode(String vendorCode);
}
