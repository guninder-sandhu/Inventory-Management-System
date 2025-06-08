package com.inventory.orderservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRetrievedDto {

    private String productId;

    private String productCode;

    private String productName;

    private double productPrice;

    private int quantity;

    private String status;
}
