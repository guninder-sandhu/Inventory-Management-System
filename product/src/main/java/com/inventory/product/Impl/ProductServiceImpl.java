package com.inventory.product.Impl;


import com.inventory.product.dto.ProductDto;
import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import com.inventory.product.exceptions.CreationException;
import com.inventory.product.exceptions.DeletionException;
import com.inventory.product.exceptions.NotFoundException;
import com.inventory.product.exceptions.UpdateException;
import com.inventory.product.repositories.ProductCountRepository;
import com.inventory.product.repositories.ProductRepository;
import com.inventory.product.services.ProductCategoryService;
import com.inventory.product.services.ProductService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductCountRepository productCountRepository;
    private final ProductCategoryService productCategoryService;

    public ProductServiceImpl(ProductRepository repository, ProductCountRepository productCountRepository, ProductCategoryService productCategoryService) {
        this.repository = repository;
        this.productCountRepository = productCountRepository;
        this.productCategoryService = productCategoryService;
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        try {
            var uniqueUUID = UUID.randomUUID().toString();
            product.setProductId(uniqueUUID);
            var productCount = getProductCount();
            var code = generateProductCode(productCount);
            product.setProductCode(code);
            updateProductCount(++productCount);
            return repository.save(product);
        } catch (Exception e) {
            throw new CreationException("Unable to create product");
        }
    }

    private int getProductCount() {
        return productCountRepository.getProductCount();
    }

    public String generateProductCode(int productCount) {
        return String.format("PROD%05d", ++productCount);
    }


    public void updateProductCount(int productCount) {
        productCountRepository.updateProductCount(productCount);
    }

    @Override
    public Product getProductById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find product with id -" + id));
    }

    @Override
    @Transactional
    public boolean updateProductById(String id, Product updatedProduct) {
        return updateProduct(getProductById(id), updatedProduct);
    }

    @Override
    @Transactional
    public boolean updateProductByCode(String code, Product product) {
        return updateProduct(getProductByCode(code), product);
    }

    private boolean updateProduct(Product retrievedProduct, Product updatedProduct) {
        if (retrievedProduct != null) {
            if (StringUtils.isBlank(updatedProduct.getProductId())) {
                updatedProduct.setProductId(retrievedProduct.getProductId());
            }
            if (StringUtils.isBlank(updatedProduct.getProductCode())) {
                updatedProduct.setProductCode(retrievedProduct.getProductCode());
            }
            try {
                repository.save(updatedProduct);
            } catch (Exception e) {
                throw new UpdateException("Unable to update product - " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteProductById(String productId) {
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
    @Transactional
    public void deleteProductByCode(String productCode) {
        if (!checkProductExistsByCode(productCode)) {
            throw new NotFoundException("Unable to find product with code -" + productCode);
        }
        try {
            repository.deleteProductByProductCode(productCode);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete product with code " + productCode + e.getMessage());
        }

    }

    @Override
    public Boolean checkProductExists(String id) {
        return repository.existsById(id);
    }

    @Override
    public boolean checkProductExistsByCode(String productCode) {
        return repository.existsByProductCode(productCode);
    }

    @Override
    @Transactional
    public Product createProductFromDto(ProductDto productDto) {
        var productCategoryName = productDto.getProductCategoryName();
        if (StringUtils.isBlank(productCategoryName)) {
            return createProduct(retrieveProduct(productDto));
        }
        try {
            var productCategory = productCategoryService.getProductCategoriesByProductCategoryName(productCategoryName);
            if(productCategory==null){
                throw new CreationException("Unable to create product as Category " + productCategoryName + " does not exist");
            }
            Product product = retrieveProduct(productDto);
            product.setProductCategory(productCategory);
            return createProduct(product);
        } catch (Exception e) {
            throw new CreationException("Unable to create product "+ e.getMessage());
        }
    }

    private Product retrieveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setProductDescription(productDto.getProductDescription());
        product.setProductPrice(productDto.getProductPrice());
        return product;
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

    @Override
    public Product getProductByCode(String code) {
        var product = repository.getProductByProductCode(code);
        if (product == null) {
            throw new NotFoundException("Unable to find product with code -" + code);
        }
        return product;
    }
}
