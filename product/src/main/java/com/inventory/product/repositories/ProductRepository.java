package com.inventory.product.repositories;

import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> getProductsByProductCategory(ProductCategory category);

    List<Product> getProductsByProductName(String name);

    @Modifying
    @Query("UPDATE Product SET productCategory = :category where productId= :id")
    void updateProductCategory(@Param("category") ProductCategory category, @Param("id") String id);

}
