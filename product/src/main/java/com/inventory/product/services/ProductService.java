package com.inventory.product.services;


import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;

import java.util.List;


public interface ProductService {

    Product createProduct(Product product);

    Product getProduct(String id);

    Boolean updateProduct(String id, Product product);

    void deleteProduct(String id);

    List<Product> getProductFromName(String name);

    List<Product> getAllProducts();

    Boolean checkProductExists(String id);

    List<Product> getProductsByCategory(ProductCategory category);

    boolean updateProductAddCategory(String id, ProductCategory category);
}
