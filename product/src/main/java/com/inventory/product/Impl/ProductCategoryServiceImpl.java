package com.inventory.product.Impl;

import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import com.inventory.product.exceptions.*;
import com.inventory.product.repositories.CategoryCountRepository;
import com.inventory.product.repositories.ProductCategoryRepository;
import com.inventory.product.repositories.ProductRepository;
import com.inventory.product.services.ProductCategoryService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final CategoryCountRepository countRepository;
    private final ProductRepository productRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository repository, CategoryCountRepository countRepository, ProductRepository productRepository) {
        this.repository = repository;
        this.countRepository = countRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        if (checkProductAlreadyExists(productCategory)) {
            throw new CreationException("Product Category already exists", HttpStatus.CONFLICT);
        }
        try {
            var uniqueUUID = UUID.randomUUID().toString();
            productCategory.setProductCategoryId(uniqueUUID);
            var categoryCount = getCategoryCount();
            var code = generateCategoryCode(categoryCount);
            productCategory.setProductCategoryCode(code);
            updateProductCount(++categoryCount);
            return repository.save(productCategory);
        } catch (Exception e) {
            throw new CreationException("Unable to create ProductCategory. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean checkProductAlreadyExists(ProductCategory productCategory) {
        return (repository.existsByProductCategoryName(productCategory.getProductCategoryName()));
    }

    private int getCategoryCount() {
        return countRepository.getCategoryCount();
    }

    public String generateCategoryCode(int categoryCount) {
        return String.format("CATG%05d", ++categoryCount);
    }

    public void updateProductCount(int productCount) {
        countRepository.updateCategoryCount(productCount);
    }

    @Override
    @Transactional
    public void updateProductCategoryById(String id, ProductCategory productCategory) {
        updateProduct(getProductCategoryById(id), productCategory);
    }

    @Override
    @Transactional
    public void updateProductCategoryByCode(String code, ProductCategory productCategory) {
        updateProduct(getProductCategoryByCode(code), productCategory);
    }

    private void updateProduct(ProductCategory retrievedCategory, ProductCategory productCategory) {
        if (retrievedCategory != null) {
            if (StringUtils.isBlank(productCategory.getProductCategoryId())) {
                productCategory.setProductCategoryId(retrievedCategory.getProductCategoryId());
            }
            if (StringUtils.isBlank(productCategory.getProductCategoryCode())) {
                productCategory.setProductCategoryCode(retrievedCategory.getProductCategoryCode());
            }
            try {
                repository.save(productCategory);
            } catch (NotFoundException ne) {
                throw ne;
            } catch (Exception e) {
                throw new UpdateException("Unable to update ProductCategory. " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void deleteProductCategoryById(String id) {
        if (!checkProductCategoryExistsById(id)) {
            throw new NotFoundException("Unable to find product with id -" + id);
        }
        try {
            var categoryUsedInProductTable = isCategoryUsedInProductTable(getProductCategoryById(id));
            if (categoryUsedInProductTable.isPresent()) {
                throw new DeletionException("Unable to delete product with id -" + id + " because" +
                        " it's linked with " + categoryUsedInProductTable.get());
            }
            repository.deleteById(id);
        } catch (DeletionException | NotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new DeletionException("Unable to delete product with id " + id + e.getMessage());
        }
    }

    private Optional<String> isCategoryUsedInProductTable(ProductCategory productCategory) {
        var linkedProducts = productRepository.getProductsByProductCategory(productCategory);
        if (!linkedProducts.isEmpty()) {
            return Optional.of(linkedProducts.stream().map(Product::getProductCode).collect(Collectors.joining(",")));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteProductCategoryByCode(String categoryCode) {
        if (!checkProductCategoryExistsByCode(categoryCode)) {
            throw new NotFoundException("Unable to find product with category code -" + categoryCode);
        }
        try {
            var categoryUsedInProductTable = isCategoryUsedInProductTable(getProductCategoryByCode(categoryCode));
            if (categoryUsedInProductTable.isPresent()) {
                throw new DeletionException("Unable to delete category with code -" + categoryCode + " because " +
                        "it's linked with " + categoryUsedInProductTable.get());
            }
            repository.deleteProductCategoryByProductCategoryCode(categoryCode);
        } catch (DeletionException | NotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new DeletionException("Unable to delete category with category code " + categoryCode + e.getMessage());
        }
    }

    @Override
    public boolean checkProductCategoryExistsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public boolean checkProductCategoryExistsByCode(String code) {
        return repository.existsByProductCategoryCode(code);
    }

    @Override
    public boolean checkProductCategoryExistsByName(String productCategoryName) {
        return repository.existsByProductCategoryName(productCategoryName);
    }

    @Override
    public ProductCategory getProductCategoriesByProductCategoryName(String productCategoryName) {
        var productCategory = repository.getProductCategoriesByProductCategoryName(productCategoryName);
        if (productCategory == null) {
            throw new NotFoundException("Category " + productCategoryName + " Doesnt Exist");
        }
        return productCategory;
    }

    @Override
    public ProductCategory getProductCategoryByCode(String code) {
        var productCategory = repository.getProductCategoriesByProductCategoryCode(code);
        if (productCategory == null) {
            throw new NotFoundException("Unable to find category with code -" + code);
        }
        return productCategory;
    }

    @Override
    public List<ProductCategory> getProductCategories() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve all product categories " + e.getMessage());
        }
    }

    @Override
    public ProductCategory getProductCategoryById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find product category with id -" + id));
    }
}
