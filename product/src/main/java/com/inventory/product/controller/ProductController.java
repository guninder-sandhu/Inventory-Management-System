package com.inventory.product.controller;

import com.inventory.product.dto.ProductDto;
import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import com.inventory.product.response.ApiResponse;
import com.inventory.product.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller to manage product-related operations.
 * <p>
 * This controller provides endpoints for creating, retrieving, updating,
 * and deleting product details. It also allows operations such as adding
 * categories to products using various HTTP methods.
 * <p>
 * Endpoints:
 * - Create a product
 * - Retrieve a product by ID or code
 * - Get a list of all products
 * - Update a product by ID or code
 * - Delete a product by ID or code
 * - Add a category to a product
 * <p>
 * It uses a service layer to perform business logic and returns responses
 * that include the status, timestamp, and data or message for each operation.
 * <p>
 * All endpoints return a generic API response containing the necessary information
 * about the requested operation.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductDto productDto) {
        ApiResponse<Product> response = new ApiResponse<>(
                "Product created successfully",
                HttpStatus.CREATED.value(),
                LocalDateTime.now(),
                service.createProductFromDto(productDto));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable String productId) {
        ApiResponse<Product> response = new ApiResponse<>(
                "Product retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductById(productId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/totalValue")
    public ResponseEntity<ApiResponse<Double>> getTotalInventoryValue() {
        ApiResponse<Double> response = new ApiResponse<>(
                "Total Inventory retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getTotalProductInventoryCost());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/lowStockProducts")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockProducts(@RequestParam(defaultValue = "10") int lowStockThreshold) {
        ApiResponse<List<Product>> response = new ApiResponse<>(
                "Low Stock Products retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductsLowInStock(lowStockThreshold));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/lowStockCount")
    public ResponseEntity<ApiResponse<Integer>> getLowStockProductsCount(@RequestParam(defaultValue = "10")  int lowStockThreshold) {
        ApiResponse<Integer> response = new ApiResponse<>(
                "Low Stock Products retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getNumberOfProductsInLowStock(lowStockThreshold));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getTotalProductCount() {
        ApiResponse<Integer> response = new ApiResponse<>(
                "Product Count retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductCount());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{productCode}")
    public ResponseEntity<ApiResponse<Product>> getProductByCode(@PathVariable String productCode) {
        ApiResponse<Product> response = new ApiResponse<>(
                "Product retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductByCode(productCode));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Product>>> getProducts() {
        ApiResponse<List<Product>> response = new ApiResponse<>(
                "Products retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getAllProducts());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateById/{productId}")
    public ResponseEntity<ApiResponse<Void>> updateProductById(@PathVariable String productId, @RequestBody Product product) {
        service.updateProductById(productId, product);
        ApiResponse<Void> response = new ApiResponse<>(
                "Product " + productId + " Updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update/{productCode}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(@PathVariable String productCode, @RequestBody Product product) {
        service.updateProductByCode(productCode, product);
        ApiResponse<Void> response = new ApiResponse<>(
                "Product " + productCode + " Updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteById/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductById(@PathVariable String productId) {
        service.deleteProductById(productId);
        ApiResponse<Void> response = new ApiResponse<>(
                "Product " + productId + " deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{productCode}")
    public ResponseEntity<ApiResponse<Void>> deleteProductByCode(@PathVariable String productCode) {
        service.deleteProductByCode(productCode);
        ApiResponse<Void> response = new ApiResponse<>(
                "Product " + productCode + " deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addCategory/{productId}")
    public ResponseEntity<ApiResponse<Void>> addCategoryToProduct(@PathVariable String productId, @RequestBody ProductCategory category) {
        service.updateProductAddCategory(productId, category);
        ApiResponse<Void> response = new ApiResponse<>(
                "Category " + category.getProductCategoryName() + " added to product " + productId + " successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
