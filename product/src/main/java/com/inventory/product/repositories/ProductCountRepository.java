package com.inventory.product.repositories;

import com.inventory.product.entities.ProductCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductCountRepository extends JpaRepository<ProductCount, Integer> {

    @Modifying
    @Query("UPDATE ProductCount SET productCount = :count where id= 1")
    void updateProductCount(@Param("count") Integer count);

    @Query("select productCount from ProductCount where id=1")
    Integer getProductCount();

}
