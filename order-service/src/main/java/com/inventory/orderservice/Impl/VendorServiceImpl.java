package com.inventory.orderservice.Impl;

import com.inventory.orderservice.dto.VendorDto;
import com.inventory.orderservice.entities.Vendor;
import com.inventory.orderservice.exceptions.*;
import com.inventory.orderservice.repository.VendorCountRepository;
import com.inventory.orderservice.repository.VendorRepo;
import com.inventory.orderservice.response.VendorCreationResult;
import com.inventory.orderservice.services.VendorService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
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
        return String.format("VEND%05d", vendorCount);
    }

    @Transactional
    @Override
    public VendorCreationResult createVendor(VendorDto vendorDto) {
        try {
            if (vendorDto == null || vendorDto.getVendorName() == null || vendorDto.getVendorContactPersonName() == null || vendorDto.getContactPhoneNumber() == null) {
                log.error("Vendor cannot be null");
                throw new CreationException("Vendor details: name,contact person,contact phone cannot be empty", HttpStatus.BAD_REQUEST);
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
        } catch (CreationException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Error creating Vendor {}", e.getMessage());
            throw new CreationException("Unable to create Vendor" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    @Override
    public Vendor updateVendor(Vendor vendor) {
        try {
            if (vendor == null || vendor.getVendorCode() == null) {
                return null;
            }
            var vendorCode = vendor.getVendorCode();
            var vendorId = repo.getVendorIdByVendorCode(vendorCode);
            if (vendorId == null) {
                throw new NotFoundException("Unable to find vendor with code " + vendorCode);
            }
            repo.updateVendorDetails(vendorId, vendor.getVendorName(), vendor.getAddress(), vendor.getContactPhoneNumber(), vendor.getVendorContactPersonName());
            return repo.findByVendorCode(vendorCode);
        } catch (UpdateException | NotFoundException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Error updating Vendor{} {}", vendor.getVendorCode(), e.getMessage());
            throw new UpdateException("Unable to update Vendor" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void deleteVendor(String vendorCode) {
        try {
            if (StringUtils.isEmpty(vendorCode)) {
                throw new DeletionException("Wrong Parameter. Unable to delete as VendorCode cant be null or empty", HttpStatus.BAD_REQUEST);
            }
            var vendor = repo.findByVendorCode(vendorCode);
            if (vendor == null) {
                throw new NotFoundException("Unable to find vendor with code " + vendorCode);
            }
            var vendorCount = vendorCountRepo.getVendorCount();
            repo.delete(vendor);
            vendorCountRepo.updateVendorCount(--vendorCount);
        } catch (DeletionException | NotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Error deleting Vendor {}", vendorCode);
            throw new DeletionException("Unable to Delete Vendor " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Vendor> findVendors() {
        try {
            return repo.findAll();
        } catch (Exception ex) {
            throw new RetrievalException("Database error" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Vendor findVendorByVendorId(String id) {
        try {
            Optional<Vendor> vendor = repo.findById(id);
            return vendor.orElseThrow(() -> new NotFoundException("Unable to find Vendor with id :" + id));
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RetrievalException("Database error" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Vendor findVendorsByVendorName(String vendorName) {
        try {
            var vendor = repo.findByVendorName(vendorName);
            return vendor.orElseThrow(() -> new NotFoundException("Unable to find Vendor with name :" + vendorName));
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RetrievalException("Database error" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Vendor findVendorsByVendorCode(String vendorCode) {
        try {
            var vendor = repo.findByVendorCode(vendorCode);
            if (vendor == null) {
                throw new NotFoundException("Unable to find Vendor with code :" + vendorCode);
            }
            return vendor;
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RetrievalException("Database error" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
