package com.inventory.product.entities;

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

    @OneToMany(mappedBy = "productCategory")
    @JsonManagedReference
    List<Product> products = new ArrayList<>();
    @Id
    @Column(name = "category_id")
    private String productCategoryId;
    @Column(name = "category_name")
    private String productCategoryName;

}
