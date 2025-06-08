package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.PurchaseOrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderItemRepo extends JpaRepository<PurchaseOrderItems, String> {

    List<PurchaseOrderItems> findAllByPurchaseOrder_OrderId(String orderId);

    List<PurchaseOrderItems> findByCreatedAt(LocalDate createdAt);

    List<PurchaseOrderItems> findByProductCode(String productCode);

    PurchaseOrderItems findByPoid(String poid);

}
