package com.inventory.orderservice.repository;

import com.inventory.orderservice.entities.Vendor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, String> {

    Optional<Vendor> findByVendorNameAndVendorContactPersonNameAndAddress(
            String vendorName, String contactPerson, String phoneNumber);

    Vendor findByVendorCode(String vendorCode);

    Optional<Vendor> findByVendorName(String vendorName);

    @Modifying
    @Transactional
    @Query("UPDATE Vendor v SET " +
            "v.vendorName = :name, " +
            "v.address = :address," +
            "v.contactPhoneNumber= :contactPhoneNumber, v.vendorContactPersonName= :vendorContactPersonName WHERE v.vendorId = :id")
    void updateVendorDetails(@Param("id") String id,
                             @Param("name") String name,
                             @Param("address") String address,
                             @Param("contactPhoneNumber") String contactPhoneNumber,
                             @Param("vendorContactPersonName") String vendorContactPersonName);

    @Query("SELECT v.vendorId FROM Vendor v WHERE v.vendorCode = :vendorCode")
    String getVendorIdByVendorCode(String vendorCode);


}
