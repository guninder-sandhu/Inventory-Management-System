package com.inventory.product.Impl;


import com.inventory.product.dto.ProductDto;
import com.inventory.product.dto.StockDto;
import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import com.inventory.product.exceptions.*;
import com.inventory.product.externalapicalls.client.StockClient;
import com.inventory.product.repositories.ProductCountRepository;
import com.inventory.product.repositories.ProductRepository;
import com.inventory.product.services.ProductCategoryService;
import com.inventory.product.services.ProductService;
import com.inventory.product.util.StockResult;
import com.inventory.product.util.StockStatus;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductCountRepository productCountRepository;
    private final ProductCategoryService productCategoryService;
    private final StockClient stockClient;

    public ProductServiceImpl(ProductRepository repository, ProductCountRepository productCountRepository, ProductCategoryService productCategoryService, StockClient stockClient) {
        this.repository = repository;
        this.productCountRepository = productCountRepository;
        this.productCategoryService = productCategoryService;
        this.stockClient = stockClient;
    }

    @Override
    @Transactional
    public Product createProductFromDto(ProductDto productDto) {
        if (productDto.getStockQuantity() < 0) {
            throw new CreationException("Unable to create product. Stock quantity should be either 0 or greater than 0", HttpStatus.BAD_REQUEST);
        }
        var productCategoryName = productDto.getProductCategoryName();
        if (StringUtils.isBlank(productCategoryName)) {
            var productWithoutCategory = createProduct(retrieveProduct(productDto));
            var stockResult = createStockForProduct(productWithoutCategory.getProductCode(), productDto.getStockQuantity());
            productWithoutCategory.setStatus(stockResult.getStatus().name());
            productWithoutCategory.setQuantity(stockResult.getQuantity());
            return productWithoutCategory;
        }
        try {
            var productCategory = productCategoryService.getProductCategoriesByProductCategoryName(productCategoryName);
            if (productCategory == null) {
                throw new CreationException("Unable to create product as Category " + productCategoryName + " does not exist", HttpStatus.BAD_REQUEST);
            }
            Product product = retrieveProduct(productDto);
            product.setProductCategory(productCategory);
            var createdProduct = createProduct(product);
            var stockResult = createStockForProduct(createdProduct.getProductCode(), productDto.getStockQuantity());
            createdProduct.setStatus(stockResult.getStatus().name());
            createdProduct.setQuantity(stockResult.getQuantity());
            return createdProduct;
        } catch (NotFoundException e) {
            throw new CreationException("Unable to create product as Category " + productCategoryName + " does not exist", HttpStatus.BAD_REQUEST);
        } catch (CreationException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationException("Unable to create product " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private StockResult createStockForProduct(String productCode, int quantity) {
        try {
            StockDto stockDto = new StockDto(productCode, quantity);
            stockClient.createStock(stockDto);
            log.info("Created stock for product with code {}", productCode);
            return quantity > 0 ? new StockResult(quantity, StockStatus.AVAILABLE) : new StockResult(quantity, StockStatus.OUT_OF_STOCK);
        } catch (FeignException.Conflict e) {
            log.warn("Stock already exists for product code: {}. Use update or modify instead.", productCode);
        } catch (FeignException.NotFound e) {
            log.warn("Stock service not found for product code: {}", productCode);
        } catch (FeignException e) {
            log.error("Feign error while creating stock for product code: {}", productCode, e);
        } catch (Exception e) {
            log.error("Unexpected error while creating stock for product code: {}", productCode, e);
        }
        return new StockResult(0, StockStatus.ERROR);
    }

    private Product retrieveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setProductDescription(productDto.getProductDescription());
        product.setProductPrice(productDto.getProductPrice());
        return product;
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
            log.info("Product {} created successfully", code);
            return repository.save(product);
        } catch (Exception e) {
            log.error("Error creating product {}", e.getMessage());
            throw new CreationException("Unable to create product" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public Product getProductByCode(String code) {
        var product = repository.getProductByProductCode(code);
        if (product == null) {
            throw new NotFoundException("Unable to find product with code " + code);
        }
        var stockStatus = getStockFromProductCode(product.getProductCode());
        product.setQuantity(stockStatus.getQuantity());
        product.setStatus(stockStatus.getStatus().name());
        log.info("Product {} retrieved successfully", code);
        return product;
    }

    @Override
    public Product getProductById(String id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unable to find product with id -" + id));
        var stockStatus = getStockFromProductCode(product.getProductCode());
        product.setQuantity(stockStatus.getQuantity());
        product.setStatus(stockStatus.getStatus().name());
        return product;
    }


    private StockResult getStockFromProductCode(String productCode) {
        log.info("Getting quantity from stock for product code: {}", productCode);
        try {
            var response = stockClient.getProductQuantityFromCode(productCode);
            if (response == null || response.getData() == null) {
                log.warn("No stock information found for product code: {}. Returning default value 0", productCode);
                return new StockResult(0, StockStatus.NOT_FOUND);
            }
            var quantity = response.getData().getQuantity();
            return quantity > 0 ? new StockResult(quantity, StockStatus.AVAILABLE) : new StockResult(quantity, StockStatus.OUT_OF_STOCK);
        } catch (FeignException.NotFound e) {
            log.warn("Stock not found for product code: {}. Returning default value 0", productCode, e);
            return new StockResult(0, StockStatus.ERROR);
        } catch (Exception e) {
            log.error("Error retrieving stock for product code: {}", productCode, e);
            return new StockResult(0, StockStatus.ERROR);
        }
    }


    @Override
    public List<Product> getProductFromName(String name) {
        return repository.getProductsByProductName(name);
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve all products " + e.getMessage());
        }
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory category) {
        return repository.getProductsByProductCategory(category);
    }

    @Override
    @Transactional
    public boolean updateProductById(String id, Product updatedProduct) {
        try {
            return updateProduct(getProductById(id), updatedProduct);
        } catch (NotFoundException ne) {
            throw ne;
        } catch (Exception e) {
            throw new UpdateException("Unable to update product " + id);
        }
    }

    @Override
    @Transactional
    public boolean updateProductByCode(String code, Product product) {
        try {
            return updateProduct(getProductByCode(code), product);
        } catch (NotFoundException ne) {
            throw ne;
        } catch (Exception e) {
            throw new UpdateException("Unable to update product " + code);
        }
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
        } catch (NotFoundException ne) {
            throw ne;
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
    public boolean updateProductAddCategory(String id, ProductCategory category) {
        if (!checkProductExists(id)) {
            throw new NotFoundException("Unable to find product with id -" + id);
        }
        try {
            repository.updateProductCategory(category, id);
            return true;
        } catch (NotFoundException ne) {
            throw ne;
        } catch (Exception e) {
            throw new UpdateException("Error in adding product category to product " + id + " " + e.getMessage());
        }
    }
}
