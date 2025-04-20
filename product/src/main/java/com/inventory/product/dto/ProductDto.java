package com.inventory.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This DTO class is being used because we want user to just pass category name
 * while creating the product
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product DTO used for creating a new product")
public class ProductDto {

    @Schema(description = "Name of the product", example = "Green Tea")
    private String productName;

    @Schema(description = "Description of the product", example = "Organic")
    private String productDescription;

    @Schema(description = "Price per item", example = "2.99")
    private double productPrice;

    @Schema(
            description = "Name of the category. Leave blank if you want to assign later.",
            example = "Beverages",
            nullable = true
    )
    private String productCategoryName;
}
