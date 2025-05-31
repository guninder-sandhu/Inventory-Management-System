package com.inventory.orderservice.services;

import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.entities.PurchaseOrderItems;

import java.util.List;

public interface PurchaseOrderItemService {
    PurchaseOrderItems createPurchaseOrderItem(PurchaseOrderItemDto purchaseOrderItems);

    boolean updatePurchaseOrderItem(PurchaseOrderItems purchaseOrderItems);

    boolean deletePurchaseOrderItem(PurchaseOrderItems purchaseOrderItems);

    boolean deletePurchaseOrderItemById(String id);

    boolean deletePurchaseOrderItemByProductId(String productId);

    List<PurchaseOrderItems> findPurchaseOrderItemsByPurchaseOrder(String purchaseOrderId);

    List<PurchaseOrderItems> findPurchaseOrderItemsById(String id);

}
