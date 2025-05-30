package com.inventory.orderservice.services;

import com.inventory.orderservice.entities.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    boolean createPurchaseOrder(PurchaseOrder purchaseOrder);

    boolean updatePurchaseOrder(PurchaseOrder purchaseOrder);

    boolean deletePurchaseOrder(String id);

    boolean deletePurchaseOrder(PurchaseOrder purchaseOrder);

    boolean deletePurchaseOrders(List<String> ids);

    List<PurchaseOrder> findPurchaseOrdersByVendorId(String id);

    boolean findPurchaseOrderById(String id);

    boolean findPurchaseOrderByIdAndVendorId(String id, String vendorId);
}
