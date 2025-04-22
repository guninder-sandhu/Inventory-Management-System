package com.inventory.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {

    private String stockId;

    private String productCode;

    private int quantity;

    public StockDto(String productCode, int quantity) {
        this.productCode = productCode;
        this.quantity = quantity;
    }
}
