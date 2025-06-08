package com.inventory.orderservice.Impl;

import com.inventory.orderservice.dto.ProductRetrievedDto;
import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.entities.PurchaseOrder;
import com.inventory.orderservice.entities.PurchaseOrderItems;
import com.inventory.orderservice.exceptions.*;
import com.inventory.orderservice.externalapiclient.clients.ProductClient;
import com.inventory.orderservice.repository.PurchaseOrderItemRepo;
import com.inventory.orderservice.services.PurchaseOrderItemService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {
    private final PurchaseOrderItemRepo repo;

    private final ProductClient client;

    public PurchaseOrderItemServiceImpl(PurchaseOrderItemRepo repo, ProductClient client) {
        this.repo = repo;
        this.client = client;
    }

    @Transactional
    @Override
    public PurchaseOrderItems createPurchaseOrderItem(PurchaseOrderItemDto items, PurchaseOrder purchaseOrder) {
        try {
            ProductRetrievedDto productRetrievedDto = getProductDetails(items.getProductCode());
            PurchaseOrderItems purchaseOrderItems = new PurchaseOrderItems();
            String orderItemId = UUID.randomUUID().toString();
            purchaseOrderItems.setPoid(orderItemId);
            purchaseOrderItems.setQuantityOrdered(items.getQuantityOrdered());
            populatePurchaseOrderItems(productRetrievedDto, purchaseOrderItems);
            purchaseOrderItems.setPurchaseOrder(purchaseOrder);
            return repo.save(purchaseOrderItems);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationException("Unable to create Purchase order item for Product code" + items.getProductCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void populatePurchaseOrderItems(ProductRetrievedDto productRetrievedDto, PurchaseOrderItems purchaseOrderItems) {
        purchaseOrderItems.setProductId(productRetrievedDto.getProductId());
        purchaseOrderItems.setProductName(productRetrievedDto.getProductName());
        purchaseOrderItems.setProductCode(productRetrievedDto.getProductCode());
        purchaseOrderItems.setUnitPrice(productRetrievedDto.getProductPrice());
    }

    private ProductRetrievedDto getProductDetails(String productCode) {
        var productRetrieved = client.getProductByCode(productCode);
        if (productRetrieved == null || productRetrieved.getBody() == null || productRetrieved.getBody().getData() == null) {
            throw new NotFoundException("Product " + productCode + "not found,so unable to create purchase Item");
        }
        var productRetrievedDto = productRetrieved.getBody().getData();
        if (StringUtils.isEmpty(productRetrievedDto.getProductId())) {
            throw new NotFoundException("Product " + productCode + "not found,so unable to create purchase Item");
        }
        return productRetrievedDto;
    }

    @Transactional
    @Override
    public PurchaseOrderItems updatePurchaseOrderItem(String poid, PurchaseOrderItemDto items) {
        try {
            var purchaseOrderItems = repo.findByPoid(poid);
            if (purchaseOrderItems == null) {
                throw new NotFoundException("No purchase order item found with id" + poid);
            }
            purchaseOrderItems.setQuantityOrdered(items.getQuantityOrdered());
            if (!purchaseOrderItems.getProductCode().equalsIgnoreCase(items.getProductCode())) {
                var productDetails = getProductDetails(items.getProductCode());
                populatePurchaseOrderItems(productDetails, purchaseOrderItems);
            }
            return repo.save(purchaseOrderItems);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new UpdateException("Unable to update order item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void deletePurchaseOrderItemById(String id) {
        try {
            var purchaseOrderItem = repo.findByPoid(id);
            if (purchaseOrderItem == null) {
                throw new NotFoundException("Purchase Order item  does not exist :" + id);
            }
            repo.delete(purchaseOrderItem);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete purchase order item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PurchaseOrderItems> findPurchaseOrderItemsByPurchaseOrder(String purchaseOrderId) {
        try {
            return repo.findAllByPurchaseOrder_OrderId(purchaseOrderId);
        } catch (Exception e) {
            throw new RetrievalException("Unable to retieve  purchase order items for order :" + purchaseOrderId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PurchaseOrderItems findPurchaseOrderItemById(String id) {
        try {
            var purchaseOrderItem = repo.findByPoid(id);
            if (purchaseOrderItem == null) {
                throw new NotFoundException("Purchase Order item  does not exist :" + id);
            }
            return purchaseOrderItem;
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve purchase order item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PurchaseOrderItems> findAllItems() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve purchase order items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PurchaseOrderItems> findAllItemsCreatedOn(LocalDate createdOn) {
        try {
            return repo.findByCreatedAt(createdOn);
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve purchase order items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}