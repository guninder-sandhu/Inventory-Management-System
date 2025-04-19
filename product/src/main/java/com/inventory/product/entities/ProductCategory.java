package com.inventory.product.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @Column(name = "category_id")
    private String productCategoryId;

    @Column(name = "category_name",unique = true,nullable = false)
    private String productCategoryName;

    @OneToMany(mappedBy = "productCategory")
    @JsonManagedReference
    @JsonIgnore
    List<Product> products = new ArrayList<>();
    @Column(name = "category_code", unique = true, nullable = false)
    private String productCategoryCode;

}
