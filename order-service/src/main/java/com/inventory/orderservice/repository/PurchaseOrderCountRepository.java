package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.PurchaseOrderCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PurchaseOrderCountRepository extends JpaRepository<PurchaseOrderCount, Integer> {

    @Modifying
    @Query("UPDATE PurchaseOrderCount SET purchaseOrderCount = :count where id= 1")
    void updatePurchaseOrderCount(@Param("count") Integer count);

    @Query("select purchaseOrderCount from PurchaseOrderCount where id=1")
    Integer getPurchaseOrderCount();

}
