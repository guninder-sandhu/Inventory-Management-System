package com.inventory.product.services;

public interface CategoryCountService {

    void updateCategoryCount(Integer productCount);

    Integer getCategoryCountById(Integer productId);
}
