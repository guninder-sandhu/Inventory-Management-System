package com.inventory.product.controller;

import com.inventory.product.entities.ProductCategory;
import com.inventory.product.response.ApiResponse;
import com.inventory.product.services.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller to manage operations related to product categories.
 * Provides endpoints to create, retrieve, update, and delete product categories
 * by ID or by category code.
 * <p>
 * Endpoints include:
 * - Creating a new product category
 * - Fetching all product categories
 * - Fetching a category by ID or code
 * - Updating a category by ID or code
 * - Deleting a category by ID or code
 */
@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

    private final ProductCategoryService service;

    public ProductCategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCategory>> createProductCategory(@RequestBody ProductCategory category) {
        ApiResponse<ProductCategory> response = new ApiResponse<>(
                "Category created successfully",
                HttpStatus.CREATED.value(),
                LocalDateTime.now(),
                service.createProductCategory(category));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/id/{categoryId}")
    public ResponseEntity<ApiResponse<ProductCategory>> getProductCategory(@PathVariable String categoryId) {
        ApiResponse<ProductCategory> response = new ApiResponse<>(
                "Category retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductCategoryById(categoryId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{categoryCode}")
    public ResponseEntity<ApiResponse<ProductCategory>> getProductCategoryByCode(@PathVariable String categoryCode) {
        ApiResponse<ProductCategory> response = new ApiResponse<>(
                "Category retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductCategoryByCode(categoryCode));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllProductCategories() {
        ApiResponse<List<ProductCategory>> response = new ApiResponse<>(
                "All Categories retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getProductCategories());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateById/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateProductById(@PathVariable String categoryId, @RequestBody ProductCategory category) {
        service.updateProductCategoryById(categoryId, category);
        ApiResponse<Void> response = new ApiResponse<>(
                "Category " + categoryId + " is updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update/{categoryCode}")
    public ResponseEntity<ApiResponse<Void>> updateProductByCode(@PathVariable String categoryCode, @RequestBody ProductCategory category) {
        service.updateProductCategoryByCode(categoryCode, category);
        ApiResponse<Void> response = new ApiResponse<>(
                "Category " + categoryCode + " is updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/deleteById/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductById(@PathVariable String categoryId) {
        service.deleteProductCategoryById(categoryId);
        ApiResponse<Void> response = new ApiResponse<>(
                "Product Category " + categoryId + " deleted successfully ",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{categoryCode}")
    public ResponseEntity<ApiResponse<Void>> deleteProductByCode(@PathVariable String categoryCode) {
        service.deleteProductCategoryByCode(categoryCode);
        ApiResponse<Void> response = new ApiResponse<>(
                "Product Category " + categoryCode + " deleted successfully ",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
