package com.inventory.product.Impl;

import com.inventory.product.entities.ProductCategory;
import com.inventory.product.exceptions.CreationException;
import com.inventory.product.exceptions.DeletionException;
import com.inventory.product.exceptions.NotFoundException;
import com.inventory.product.repositories.ProductCategoryRepository;
import com.inventory.product.services.ProductCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;

    public ProductCategoryServiceImpl(ProductCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        try {
            var uniqueUUID = UUID.randomUUID().toString();
            productCategory.setProductCategoryId(uniqueUUID);
            return repository.save(productCategory);
        } catch (Exception e) {
            throw new CreationException("Unable to create ProductCategory. " + e.getMessage());
        }
    }

    @Override
    public boolean updateProductCategory(String id, ProductCategory productCategory) {
        var retrievedCategory = getProductCategory(id);
        if (retrievedCategory != null) {
            if (StringUtils.isBlank(retrievedCategory.getProductCategoryId())) {
                retrievedCategory.setProductCategoryId(id);
            }
            repository.save(productCategory);
            return true;
        }
        return false;
    }

    @Override
    public void deleteProductCategory(String id) {
        if (!checkProductCategoryExists(id)) {
            throw new NotFoundException("Unable to find product with id -" + id);
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete product with id " + id + e.getMessage());
        }
    }

    @Override
    public boolean checkProductCategoryExists(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<ProductCategory> getProductCategories() {
        return repository.findAll();
    }

    @Override
    public ProductCategory getProductCategory(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find product category with id -" + id));
    }
}
