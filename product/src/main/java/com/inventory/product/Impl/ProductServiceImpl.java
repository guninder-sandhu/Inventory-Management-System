package com.inventory.product.Impl;


import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import com.inventory.product.exceptions.DeletionException;
import com.inventory.product.exceptions.NotFoundException;
import com.inventory.product.exceptions.UpdateException;
import com.inventory.product.repositories.ProductRepository;
import com.inventory.product.services.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        var uniqueUUID = UUID.randomUUID().toString();
        product.setProductId(uniqueUUID);
        return repository.save(product);
    }

    @Override
    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find product with id -" + id));
    }

    @Override
    public Boolean updateProduct(String id, Product product) {
        var retrievedProduct = getProduct(id);
        if (retrievedProduct != null) {
            if (StringUtils.isBlank(product.getProductId())) {
                product.setProductId(id);
            }
            repository.save(product);
            return true;
        }
        return false;
    }

    @Override
    public void deleteProduct(String productId) {
        if (!checkProductExists(productId)) {
            throw new NotFoundException("Unable to find product with id -" + productId);
        }
        try {
            repository.deleteById(productId);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete product with id " + productId + e.getMessage());
        }
    }

    @Override
    public Boolean checkProductExists(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<Product> getProductFromName(String name) {
        return repository.getProductsByProductName(name);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory category) {
        return repository.getProductsByProductCategory(category);
    }

    @Override
    public boolean updateProductAddCategory(String id, ProductCategory category) {
        if (!checkProductExists(id)) {
            throw new NotFoundException("Unable to find product with id -" + id);
        }
        try {
            repository.updateProductCategory(category, id);
            return true;
        } catch (Exception e) {
            throw new UpdateException("Error in adding product category to product " + id + " " + e.getMessage());
        }
    }
}
