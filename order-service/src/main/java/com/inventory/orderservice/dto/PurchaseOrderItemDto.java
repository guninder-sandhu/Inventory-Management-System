package com.inventory.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderItemDto {

    private String productCode;
    private String productName;
    private int quantityOrdered;
}
