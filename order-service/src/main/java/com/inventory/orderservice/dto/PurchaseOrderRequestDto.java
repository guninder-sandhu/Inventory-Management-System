package com.inventory.orderservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseOrderRequestDto {

    private String vendorCode;
    private List<PurchaseOrderItemDto> purchaseOrderItemsList;
    private String orderedBy;

}
