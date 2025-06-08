package com.inventory.orderservice.Impl;

import com.inventory.orderservice.constants.OrderStatus;
import com.inventory.orderservice.dto.PurchaseOrderItemDto;
import com.inventory.orderservice.dto.PurchaseOrderRequestDto;
import com.inventory.orderservice.entities.PurchaseOrder;
import com.inventory.orderservice.entities.PurchaseOrderItems;
import com.inventory.orderservice.exceptions.CreationException;
import com.inventory.orderservice.exceptions.DeletionException;
import com.inventory.orderservice.exceptions.NotFoundException;
import com.inventory.orderservice.exceptions.UpdateException;
import com.inventory.orderservice.repository.PurchaseOrderCountRepository;
import com.inventory.orderservice.repository.PurchaseOrderRepo;
import com.inventory.orderservice.repository.VendorRepo;
import com.inventory.orderservice.services.PurchaseOrderItemService;
import com.inventory.orderservice.services.PurchaseOrderService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final VendorRepo vendorRepo;
    private final PurchaseOrderRepo purchaseOrderRepo;
    private final PurchaseOrderItemService purchaseOrderItemService;
    private final PurchaseOrderCountRepository purchaseOrderCountRepo;

    public PurchaseOrderServiceImpl(VendorRepo vendorRepo, PurchaseOrderRepo purchaseOrderRepo, PurchaseOrderItemService purchaseOrderItemService, PurchaseOrderCountRepository purchaseOrderCountrepo) {
        this.vendorRepo = vendorRepo;
        this.purchaseOrderRepo = purchaseOrderRepo;
        this.purchaseOrderItemService = purchaseOrderItemService;
        this.purchaseOrderCountRepo = purchaseOrderCountrepo;
    }

    @Transactional
    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto) {
        try {
            var vendorCode = purchaseOrderRequestDto.getVendorCode();
            if (!vendorRepo.existsVendorByVendorCode(vendorCode)) {
                throw new CreationException("Vendor does not exist,so unable to create purchase order", HttpStatus.NOT_FOUND);
            }
            var orderCount = purchaseOrderCountRepo.getPurchaseOrderCount();
            PurchaseOrder purchaseOrder = createPurchaseOrderWithoutItems(purchaseOrderRequestDto, orderCount);
            PurchaseOrder savedPurchaseOrder = purchaseOrderRepo.save(purchaseOrder);
            populatePurchaseOrderFromDto(savedPurchaseOrder, purchaseOrderRequestDto.getPurchaseOrderItemsList());
            savedPurchaseOrder = purchaseOrderRepo.save(savedPurchaseOrder);
            purchaseOrderCountRepo.updatePurchaseOrderCount(++orderCount);
            return savedPurchaseOrder;
        } catch (NotFoundException | CreationException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationException("Unable to create Purchase Order" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private PurchaseOrder createPurchaseOrderWithoutItems(PurchaseOrderRequestDto poRequestDto, Integer orderCount) {
        var purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderId(UUID.randomUUID().toString());
        purchaseOrder.setCreateBy(poRequestDto.getOrderedBy());
        purchaseOrder.setOrderCode(generateOrderCode(++orderCount));
        purchaseOrder.setVendor(vendorRepo.findByVendorCode(poRequestDto.getVendorCode()));
        purchaseOrder.setOrderDate(new Date());
        purchaseOrder.setOrderStatus(OrderStatus.CREATED);
        purchaseOrder.setOrderCost(0.0);
        return purchaseOrder;
    }


    private void populatePurchaseOrderFromDto(PurchaseOrder purchaseOrder, List<PurchaseOrderItemDto> purchaseOrderItemsListFromDto) {
        List<PurchaseOrderItems> purchaseOrderItemsList = new ArrayList<>();
        for (PurchaseOrderItemDto purchaseOrderItemDto : purchaseOrderItemsListFromDto) {
            var purchaseOrderItem = purchaseOrderItemService.createPurchaseOrderItem(purchaseOrderItemDto, purchaseOrder);
            purchaseOrderItemsList.add(purchaseOrderItem);
        }
        purchaseOrder.setPurchaseOrderItems(purchaseOrderItemsList);
        purchaseOrder.setOrderCost(calculateCost(purchaseOrderItemsList));
    }

    private Double calculateCost(List<PurchaseOrderItems> purchaseOrderItemsList) {
        double cost = 0;
        for (PurchaseOrderItems purchaseOrderItem : purchaseOrderItemsList) {
            cost += purchaseOrderItem.getQuantityOrdered() * purchaseOrderItem.getUnitPrice();
        }
        return cost;
    }


    private String generateOrderCode(Integer orderCount) {
        return String.format("ORD-%05d", orderCount);
    }


    @Transactional
    @Override
    public PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        try {
            return purchaseOrderRepo.save(purchaseOrder);
        } catch (Exception e) {
            throw new UpdateException("Unable to update Purchase Order" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void deletePurchaseOrder(String id) {
        try {
            purchaseOrderRepo.deleteById(id);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete Purchase Order" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void deletePurchaseOrder(PurchaseOrder purchaseOrder) {
        try {
            purchaseOrderRepo.delete(purchaseOrder);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete Purchase Order" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void deletePurchaseOrderByCode(String purchaseOrderCode) {
        try {
            var purchaseOrderCount = purchaseOrderCountRepo.getPurchaseOrderCount();
            purchaseOrderRepo.deleteByOrderCode(purchaseOrderCode);
            purchaseOrderCountRepo.updatePurchaseOrderCount(++purchaseOrderCount);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DeletionException("Unable to delete Purchase Order" + purchaseOrderCode + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void deletePurchaseOrders(List<String> ids) {
        try {
            ids.forEach(this::deletePurchaseOrder);
        } catch (Exception e) {
            throw new DeletionException("Unable to delete " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PurchaseOrder> findPurchaseOrdersByVendorId(String vendorId) {
        try {
            List<PurchaseOrder> purchaseOrderList = purchaseOrderRepo.findByVendor_VendorId(vendorId);
            if (purchaseOrderList.isEmpty()) {
                throw new NotFoundException("Purchase Order Not Found");
            }
            return purchaseOrderList;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new NotFoundException("Unable to found Purchase Order for vendor vendorId :" + vendorId + " " + e.getMessage());
        }
    }

    @Override
    public PurchaseOrder findPurchaseOrderById(String id) {
        return purchaseOrderRepo.findById(id).orElseThrow(() -> new NotFoundException("Purchase Order no found "));
    }

    @Override
    public PurchaseOrder findPurchaseOrderByIdAndVendorId(String id, String vendorId) {
        try {
            var purchaseOrder = purchaseOrderRepo.findByOrderIdAndVendor_VendorId(id, vendorId);
            if (purchaseOrder == null) {
                throw new NotFoundException("Purchase Order not found ");
            }
            return purchaseOrder;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new NotFoundException("Purchase Order not found " + e.getMessage());
        }
    }

    @Override
    public PurchaseOrder findPurchaseOrderByCodeAndVendorCode(String orderCode, String vendorCode) {
        try {
            var purchaseOrder = purchaseOrderRepo.findByOrderCodeAndVendor_VendorCode(orderCode, vendorCode);
            if (purchaseOrder == null) {
                throw new NotFoundException("Purchase Order not found ");
            }
            return purchaseOrder;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new NotFoundException("Purchase Order not found " + e.getMessage());
        }
    }

    @Override
    public PurchaseOrder findPurchaseOrderByCode(String orderCode) {
        try {
            var purchaseOrder = purchaseOrderRepo.findByOrderCode(orderCode);
            if (purchaseOrder == null) {
                throw new NotFoundException("Purchase Order not found ");
            }
            return purchaseOrder;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new NotFoundException("Purchase Order not found " + e.getMessage());
        }
    }

    @Override
    public List<PurchaseOrder> findPurchaseOrderByVendorCode(String vendorCode) {
        try {
            var purchaseOrder = purchaseOrderRepo.findByVendor_VendorCode(vendorCode);
            if (purchaseOrder == null || purchaseOrder.isEmpty()) {
                throw new NotFoundException("Purchase Order not found ");
            }
            return purchaseOrder;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new NotFoundException("Purchase Order not found " + e.getMessage());
        }
    }
}
