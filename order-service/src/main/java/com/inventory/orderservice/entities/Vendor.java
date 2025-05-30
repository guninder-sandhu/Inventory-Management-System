package com.inventory.orderservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "vendor",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"vendor_name", "contact_person", "address"})
        }
)
public class Vendor {

    @Id
    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "vendor_code", nullable = false, unique = true)
    private String vendorCode;

    @Column(name = "contact_person", nullable = false)
    private String vendorContactPersonName;

    @Column(name = "phone_number", nullable = false)
    private String contactPhoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "vendor")
    private List<PurchaseOrder> orders;
}
