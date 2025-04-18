package com.inventory.product.services;

import com.inventory.product.entities.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    ProductCategory createProductCategory(ProductCategory productCategory);

    boolean updateProductCategory(String id, ProductCategory productCategory);

    void deleteProductCategory(String id);

    List<ProductCategory> getProductCategories();

    ProductCategory getProductCategory(String id);

    boolean checkProductCategoryExists(String id);

}
