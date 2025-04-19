package com.inventory.product.services;


import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;

import java.util.List;


public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(String id);

    boolean updateProductById(String id, Product product);

    boolean updateProductByCode(String code, Product product);

    void deleteProductById(String id);

    void deleteProductByCode(String code);

    List<Product> getProductFromName(String name);

    List<Product> getAllProducts();

    Boolean checkProductExists(String id);

    List<Product> getProductsByCategory(ProductCategory category);

    boolean updateProductAddCategory(String id, ProductCategory category);

    Product getProductByCode(String code);

    boolean checkProductExistsByCode(String code);
}
