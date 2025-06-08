package com.inventory.orderservice.services;

import com.inventory.orderservice.dto.PurchaseOrderRequestDto;
import com.inventory.orderservice.entities.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrder createPurchaseOrder(PurchaseOrderRequestDto purchaseOrder);

    PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder);

    void deletePurchaseOrder(String id);

    void deletePurchaseOrder(PurchaseOrder purchaseOrder);

    void deletePurchaseOrderByCode(String purchaseOrderCode);

    void deletePurchaseOrders(List<String> ids);

    List<PurchaseOrder> findPurchaseOrdersByVendorId(String id);

    PurchaseOrder findPurchaseOrderById(String id);

    PurchaseOrder findPurchaseOrderByIdAndVendorId(String id, String vendorId);

    PurchaseOrder findPurchaseOrderByCodeAndVendorCode(String orderCode, String vendorCode);

    PurchaseOrder findPurchaseOrderByCode(String orderCode);

    List<PurchaseOrder> findPurchaseOrderByVendorCode(String vendorCode);
}
