package com.inventory.product.controller;

import com.inventory.product.entities.Product;
import com.inventory.product.entities.ProductCategory;
import com.inventory.product.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable String productId) {
        Map<String, Object> response = new HashMap<>();
        var product = productService.getProduct(productId);
        response.put("product", product);
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        if (productService.updateProduct(productId, product)) {
            response.put("productId", productId);
            response.put("message", "product is updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("productId", productId);
        response.put("message", "product is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/delete/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String productId) {
        Map<String, Object> response = new HashMap<>();
        productService.deleteProduct(productId);
        response.put("productId", productId);
        response.put("message", "Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addCategory/{productId}")
    public ResponseEntity<Map<String, Object>> addCategoryToProduct(@PathVariable String productId, @RequestBody ProductCategory category) {
        Map<String, Object> response = new HashMap<>();
        if (productService.updateProductAddCategory(productId, category)) {
            response.put("productId", productId);
            response.put("message", "category added to product");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("productId", productId);
        response.put("message", "product is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
