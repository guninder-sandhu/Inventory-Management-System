package com.inventory.product.controller;

import com.inventory.product.dto.ProductDto;
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
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {

        return new ResponseEntity<>(productService.createProductFromDto(productDto), HttpStatus.CREATED);
    }



    @GetMapping("/id/{productId}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable String productId) {
        Map<String, Object> response = new HashMap<>();
        var product = productService.getProductById(productId);
        response.put("product", product);
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{productCode}")
    public ResponseEntity<Map<String, Object>> getProductByCode(@PathVariable String productCode) {
        Map<String, Object> response = new HashMap<>();
        var product = productService.getProductByCode(productCode);
        response.put("product", product);
        response.put("message", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("/updateById/{productId}")
    public ResponseEntity<Map<String, Object>> updateProductById(@PathVariable String productId, @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        if (productService.updateProductById(productId, product)) {
            response.put("productId", productId);
            response.put("message", "product is updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("productId", productId);
        response.put("message", "product is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update/{productCode}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable String productCode, @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        if (productService.updateProductByCode(productCode, product)) {
            response.put("productCode", productCode);
            response.put("message", "product is updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("productCode", productCode);
        response.put("message", "product is not updated successfully");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/deleteById/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProductById(@PathVariable String productId) {
        Map<String, Object> response = new HashMap<>();
        productService.deleteProductById(productId);
        response.put("productId", productId);
        response.put("message", "Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{productCode}")
    public ResponseEntity<Map<String, Object>> deleteProductByCode(@PathVariable String productCode) {
        Map<String, Object> response = new HashMap<>();
        productService.deleteProductByCode(productCode);
        response.put("product Code", productCode);
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
