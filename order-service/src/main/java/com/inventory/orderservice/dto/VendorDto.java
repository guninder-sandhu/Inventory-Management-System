package com.inventory.orderservice.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {

    private String vendorName;

    private String vendorContactPersonName;

    private String contactPhoneNumber;

    private String address;

}
