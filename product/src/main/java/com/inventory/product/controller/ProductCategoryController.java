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
        var productCategory = service.createProductCategory(category);
        return new ResponseEntity<>(productCategory, HttpStatus.CREATED);
    }

    @GetMapping("/id/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductCategory(@PathVariable String categoryId) {
        Map<String, Object> response = new HashMap<>();
        var product = service.getProductCategoryById(categoryId);
        response.put("category", product);
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{categoryCode}")
    public ResponseEntity<Map<String, Object>> getProductCategoryByCode(@PathVariable String categoryCode) {
        Map<String, Object> response = new HashMap<>();
        var product = service.getProductCategoryByCode(categoryCode);
        response.put("category", product);
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        return new ResponseEntity<>(service.getProductCategories(), HttpStatus.OK);
    }

    @PostMapping("/updateById/{categoryId}")
    public ResponseEntity<Map<String, Object>> updateProductById(@PathVariable String categoryId, @RequestBody ProductCategory category) {
        Map<String, Object> response = new HashMap<>();
        if (service.updateProductCategoryById(categoryId, category)) {
            response.put("categoryId", categoryId);
            response.put("message", "category is updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("categoryId", categoryId);
        response.put("message", "category is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update/{categoryCode}")
    public ResponseEntity<Map<String, Object>> updateProductByCode(@PathVariable String categoryCode, @RequestBody ProductCategory category) {
        Map<String, Object> response = new HashMap<>();
        if (service.updateProductCategoryByCode(categoryCode, category)) {
            response.put("category code", categoryCode);
            response.put("message", "category is updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("category code", categoryCode);
        response.put("message", "category is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/deleteById/{categoryId}")
    public ResponseEntity<Map<String, Object>> deleteProductById(@PathVariable String categoryId) {
        Map<String, Object> response = new HashMap<>();
        service.deleteProductCategoryById(categoryId);
        response.put("categoryId", categoryId);
        response.put("message", "Product Category  deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{categoryCode}")
    public ResponseEntity<Map<String, Object>> deleteProductByCode(@PathVariable String categoryCode) {
        Map<String, Object> response = new HashMap<>();
        service.deleteProductCategoryByCode(categoryCode);
        response.put("category code", categoryCode);
        response.put("message", "Product Category  deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
