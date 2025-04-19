package com.inventory.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductCount {

    @Id
    @Column(name = "id")
    private int id = 1;   //fixed id as this table just stores count so only one row

    @Column(name = "count")
    private Integer productCount = 0;

}
