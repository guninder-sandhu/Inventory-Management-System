package com.inventory.orderservice.Impl;

import com.inventory.orderservice.dto.VendorDto;
import com.inventory.orderservice.entities.Vendor;
import com.inventory.orderservice.repository.VendorCountRepository;
import com.inventory.orderservice.repository.VendorRepo;
import com.inventory.orderservice.response.VendorCreationResult;
import com.inventory.orderservice.services.VendorService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepo repo;
    private final VendorCountRepository vendorCountRepo;

    public VendorServiceImpl(VendorRepo repo, VendorCountRepository vendorCountRepo) {
        this.repo = repo;
        this.vendorCountRepo = vendorCountRepo;
    }


    private String generateVendorCode(Integer vendorCount) {
        return String.format("VENDOR%05d", vendorCount);
    }

    @Transactional
    @Override
    public VendorCreationResult createVendor(VendorDto vendorDto) {
        if (vendorDto == null) {
            log.error("Vendor cannot be null");
            return null;
        }
        Optional<Vendor> existingVendor = repo
                .findByVendorNameAndVendorContactPersonNameAndAddress(
                        vendorDto.getVendorName(),
                        vendorDto.getVendorContactPersonName(),
                        vendorDto.getAddress()
                );

        if (existingVendor.isPresent()) {
            return new VendorCreationResult(existingVendor.get(), true);
        }
        Vendor vendor = new Vendor();
        String vendorId = UUID.randomUUID().toString();
        vendor.setVendorId(vendorId);
        vendor.setVendorName(vendorDto.getVendorName());
        var vendorCount = vendorCountRepo.getVendorCount();
        vendor.setVendorCode(generateVendorCode(++vendorCount));
        vendor.setAddress(vendorDto.getAddress());
        vendor.setContactPhoneNumber(vendorDto.getContactPhoneNumber());
        vendor.setVendorContactPersonName(vendorDto.getVendorContactPersonName());
        repo.save(vendor);
        vendorCountRepo.updateVendorCount(vendorCount);
        return new VendorCreationResult(vendor, false);
    }


    @Override
    public Vendor updateVendor(Vendor vendor) {
        return null;
    }

    @Override
    public void deleteVendor(String id) {

    }

    @Override
    public List<Vendor> findVendors() {
        return List.of();
    }

    @Override
    public Vendor findVendorByVendorId(String id) {
        return null;
    }

    @Override
    public Vendor findVendorsByVendorName(String vendorName) {
        return null;
    }
}
