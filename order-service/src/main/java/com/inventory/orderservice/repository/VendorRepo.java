package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, String> {

    Optional<Vendor> findByVendorNameAndVendorContactPersonNameAndAddress(
            String vendorName, String contactPerson, String phoneNumber);

}
