package com.inventory.product.controller;

import com.inventory.product.entities.ProductCategory;
import com.inventory.product.services.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

    private final ProductCategoryService service;

    public ProductCategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductCategory> createProductCategory(@RequestBody ProductCategory category) {
        return new ResponseEntity<>(service.createProductCategory(category), HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductCategory(@PathVariable String categoryId) {
        Map<String, Object> response = new HashMap<>();
        var product = service.getProductCategory(categoryId);
        response.put("category", product);
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        return new ResponseEntity<>(service.getProductCategories(), HttpStatus.OK);
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable String categoryId, @RequestBody ProductCategory category) {
        Map<String, Object> response = new HashMap<>();
        if (service.updateProductCategory(categoryId, category)) {
            response.put("categoryId", categoryId);
            response.put("message", "category is updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("categoryId", categoryId);
        response.put("message", "category is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String categoryId) {
        Map<String, Object> response = new HashMap<>();
        service.deleteProductCategory(categoryId);
        response.put("categoryId", categoryId);
        response.put("message", "Product Category  deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
