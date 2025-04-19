package com.inventory.product.services;

public interface ProductCountService {

    void updateProductCount(Integer productCount);

    Integer getProductCountById(Integer productId);
}
