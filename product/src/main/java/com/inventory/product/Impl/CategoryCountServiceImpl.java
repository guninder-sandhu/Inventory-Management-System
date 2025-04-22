package com.inventory.product.Impl;

import com.inventory.product.entities.CategoryCount;
import com.inventory.product.exceptions.CreationException;
import com.inventory.product.exceptions.NotFoundException;
import com.inventory.product.exceptions.UpdateException;
import com.inventory.product.repositories.CategoryCountRepository;
import com.inventory.product.services.CategoryCountService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryCountServiceImpl implements CategoryCountService {

    private final CategoryCountRepository repository;

    public CategoryCountServiceImpl(CategoryCountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @PostConstruct
    public void initializeProductCount() {
        var categoryCount = repository.findById(1);
        if (categoryCount.isEmpty()) {
            try {
                var freshCount = new CategoryCount(1, 0);
                repository.save(freshCount);
            } catch (Exception e) {
                throw new CreationException("Unable to create Category Count" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void updateCategoryCount(Integer CategoryCount) {
        if (!repository.existsById(1)) {
            throw new NotFoundException("Category Count not found");
        }
        try {
            repository.updateCategoryCount(CategoryCount);
        } catch (Exception e) {
            throw new UpdateException("Error while updating category count");
        }

    }

    @Override
    public Integer getCategoryCountById(Integer category) {
        var categoryCount = repository.findById(1).orElseThrow(() -> new NotFoundException("Category Count not found"));
        return categoryCount.getCategoryCount();
    }

}
