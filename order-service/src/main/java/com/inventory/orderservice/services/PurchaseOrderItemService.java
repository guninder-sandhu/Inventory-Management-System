package com.inventory.orderservice.services;

import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.entities.PurchaseOrderItems;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseOrderItemService {
    PurchaseOrderItems createPurchaseOrderItem(PurchaseOrderItemDto purchaseOrderItems);

    PurchaseOrderItems updatePurchaseOrderItem(String poid, PurchaseOrderItemDto purchaseOrderItems);

    void deletePurchaseOrderItemById(String id);

    List<PurchaseOrderItems> findPurchaseOrderItemsByPurchaseOrder(String purchaseOrderId);

    PurchaseOrderItems findPurchaseOrderItemById(String id);

    List<PurchaseOrderItems> findAllItems();

    List<PurchaseOrderItems> findAllItemsCreatedOn(LocalDate createdOn);

}
