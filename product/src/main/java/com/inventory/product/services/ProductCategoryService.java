package com.inventory.product.services;

import com.inventory.product.entities.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    ProductCategory createProductCategory(ProductCategory productCategory);

    List<ProductCategory> getProductCategories();

    ProductCategory getProductCategoryById(String id);

    ProductCategory getProductCategoryByCode(String code);

    boolean updateProductCategoryById(String id, ProductCategory productCategory);

    boolean updateProductCategoryByCode(String code, ProductCategory productCategory);

    boolean checkProductCategoryExistsById(String id);

    boolean checkProductCategoryExistsByCode(String code);

    void deleteProductCategoryById(String id);

    void deleteProductCategoryByCode(String categoryCode);

}
