package com.inventory.product.repositories;

import com.inventory.product.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    ProductCategory getProductCategoriesByProductCategoryCode(String Code);

    void deleteProductCategoryByProductCategoryCode(String categoryCode);

    boolean existsByProductCategoryCode(String productCategoryCode);

    boolean existsByProductCategoryName(String productCategoryName);

    ProductCategory getProductCategoriesByProductCategoryName(String productCategoryName);
}
