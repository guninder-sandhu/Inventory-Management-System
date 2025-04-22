package com.inventory.product.Impl;

import com.inventory.product.entities.ProductCount;
import com.inventory.product.exceptions.CreationException;
import com.inventory.product.exceptions.NotFoundException;
import com.inventory.product.exceptions.UpdateException;
import com.inventory.product.repositories.ProductCountRepository;
import com.inventory.product.services.ProductCountService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCountServiceImpl implements ProductCountService {

    private final ProductCountRepository repository;

    public ProductCountServiceImpl(ProductCountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @PostConstruct
    public void initializeProductCount() {
        Optional<ProductCount> productCount = repository.findById(1);
        if (productCount.isEmpty()) {
            try {
                ProductCount freshCount = new ProductCount(1, 0);
                repository.save(freshCount);
            } catch (Exception e) {
                throw new CreationException("Unable to create ProductCount" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    @Override
    public void updateProductCount(Integer productCount) {
        if (!repository.existsById(1)) {
            throw new NotFoundException("Product Count not found");
        }
        try {
            repository.updateProductCount(productCount);
        } catch (Exception e) {
            throw new UpdateException("Error while updating product count");
        }

    }

    @Override
    public Integer getProductCountById(Integer productId) {
        var productCount = repository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        return productCount.getProductCount();
    }

}
