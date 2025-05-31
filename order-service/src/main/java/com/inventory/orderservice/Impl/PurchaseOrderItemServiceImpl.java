package com.inventory.orderservice.Impl;

import com.inventory.orderservice.dto.ProductRetrievedDto;
import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.entities.PurchaseOrderItems;
import com.inventory.orderservice.exceptions.CreationException;
import com.inventory.orderservice.exceptions.NotFoundException;
import com.inventory.orderservice.externalapiclient.clients.ProductClient;
import com.inventory.orderservice.repository.PurchaseOrderItemRepo;
import com.inventory.orderservice.services.PurchaseOrderItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    @Override
    public PurchaseOrderItems createPurchaseOrderItem(PurchaseOrderItemDto items) {
        try {
            ProductRetrievedDto productRetrievedDto = getProductDetails(items.getProductCode());
            var purchaseOrderItems = populatePurchaseOrderItems(productRetrievedDto);
            purchaseOrderItems.setQuantityOrdered(items.getQuantityOrdered());
            repo.save(purchaseOrderItems);
            return purchaseOrderItems;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationException("Unable to create Purchase order item for Product code" + items.getProductCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PurchaseOrderItems populatePurchaseOrderItems(ProductRetrievedDto productRetrievedDto) {
        PurchaseOrderItems purchaseOrderItems = new PurchaseOrderItems();
        String orderItemId = UUID.randomUUID().toString();
        purchaseOrderItems.setPoid(orderItemId);
        purchaseOrderItems.setProductId(productRetrievedDto.getProductId());
        purchaseOrderItems.setProductName(productRetrievedDto.getProductName());
        purchaseOrderItems.setUnitPrice(productRetrievedDto.getProductPrice());
        return purchaseOrderItems;
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


    @Override
    public boolean updatePurchaseOrderItem(PurchaseOrderItems purchaseOrderItems) {
        return false;
    }

    @Override
    public boolean deletePurchaseOrderItem(PurchaseOrderItems purchaseOrderItems) {
        return false;
    }

    @Override
    public boolean deletePurchaseOrderItemById(String id) {
        return false;
    }

    @Override
    public boolean deletePurchaseOrderItemByProductId(String productId) {
        return false;
    }

    @Override
    public List<PurchaseOrderItems> findPurchaseOrderItemsByPurchaseOrder(String purchaseOrderId) {
        return List.of();
    }

    @Override
    public List<PurchaseOrderItems> findPurchaseOrderItemsById(String id) {
        return List.of();
    }
}
