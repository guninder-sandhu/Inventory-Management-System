package com.inventory.product.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product")
public class Product {

    @Id
    private String productId;

    @Column(name = "product_code", unique = true, nullable = false)
    private String productCode;

    @Column(name = "name", length = 100)
    private String productName;

    @Column(name = "description", length = 100)
    private String productDescription;

    @Column(name = "price_per_item")
    private double productPrice;

    //Including jsonIgnore so that when Jackson converts Product to Json ,it ignores ProductCategory
    //because this was causing infinite recursion
    //This creates a bidirectional relationship — ProductCategory has Products, and each Product has a Category.
    //Now when Jackson (used by Spring Boot for JSON serialization) tries to serialize ProductCategory, it sees:
    //ProductCategory → List<Product> → ProductCategory → List<Product> → ...
    //That’s an infinite loop, which crashes or creates a huge nested object.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private ProductCategory productCategory;

    //Add custom getter as we need category name in Product json
    // Bypasses the @JsonIgnore (because it’s a different method),
    //Adds a field productCategoryName to your JSON,
    //And only includes the name, not the full category object (which could cause recursion again).
    @JsonProperty("productCategoryName")
    public String getProductCategoryName() {
        return productCategory != null ? productCategory.getProductCategoryName() : null;
    }

    @Transient
    private int quantity;

    @Transient
    private String status;
}