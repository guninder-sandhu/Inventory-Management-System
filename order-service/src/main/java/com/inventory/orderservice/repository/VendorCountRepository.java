package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.VendorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VendorCountRepository extends JpaRepository<VendorCount, Integer> {

    @Modifying
    @Query("UPDATE VendorCount SET vendorCount = :count where id= 1")
    void updateVendorCount(@Param("count") Integer count);

    @Query("select vendorCount from VendorCount where id=1")
    Integer getVendorCount();

}
