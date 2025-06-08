package com.inventory.orderservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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

    @Column(name = "vendor_code", nullable = false, unique = true, updatable = false)
    private String vendorCode;

    @Column(name = "contact_person", nullable = false)
    private String vendorContactPersonName;

    @Column(name = "phone_number", nullable = false)
    @Pattern(
            regexp = "^\\+?[0-9 ]{7,20}$",
            message = "Phone number must be valid and contain only digits, optionally starting with +"
    )
    private String contactPhoneNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "vendor")
    @JsonBackReference
    private List<PurchaseOrder> orders;
}
