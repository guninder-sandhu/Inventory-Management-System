package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.PurchaseOrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderItemRepo extends JpaRepository<PurchaseOrderItems, String> {
}
