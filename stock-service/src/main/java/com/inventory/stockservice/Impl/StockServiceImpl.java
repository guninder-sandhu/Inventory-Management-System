package com.inventory.stockservice.Impl;

import com.inventory.stockservice.entities.Stock;
import com.inventory.stockservice.exceptions.*;
import com.inventory.stockservice.repository.StockRepository;
import com.inventory.stockservice.service.StockService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository repository;
    private final static String PRODUCT_CODE = "productcode";
    private final static String STOCK_ID = "stockid";

    public StockServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public Stock findByProductCode(String productCode) {
        var stock = repository.getStockByProductCode(productCode);
        log.info("stock retrieved {}", stock.getQuantity());
        return stock;
    }

    @Override
    public Stock createStock(Stock stock) {
        if (repository.existsByProductCode(stock.getProductCode())) {
            throw new CreationException("Product code " + stock.getProductCode() + " already exists.Use update or modify");
        }
        var stockId = UUID.randomUUID().toString();
        stock.setStockId(stockId);
        return repository.save(stock);
    }

    @Override
    public Stock findById(String id) {
        return repository.getStocksByStockId(id);
    }

    @Override
    public List<Stock> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Stock> filterStockByQuantity(String type, int quantity) {
        try {
            return switch (type.toLowerCase()) {
                case "above" -> repository.getProductStockAboveSomeQuantity(quantity);
                case "below" -> repository.getProductStockBelowSomeQuantity(quantity);
                case "equal" -> repository.getProductStockEqualToQuantity(quantity);
                default ->
                        throw new WrongParameterException("Invalid filter type: " + type + ".Use 'above','below' or 'equal' ");
            };
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve quantity details for filter " + type);
        }

    }

    @Override
    @Transactional
    public void updateStockQuantity(String by, String value, int quantity, int version) {
        if (quantity < 0) {
            throw new WrongParameterException("Quantity must be greater than 0");
        }
        try {
            switch (by.toLowerCase()) {
                case PRODUCT_CODE -> {
                    if (!repository.existsByProductCode(value)) {
                        throw new NotFoundException("Unable to find product " + value);
                    }
                    var updatedRecords = repository.updateStockQuantityByProductCode(value, version, quantity);
                    if (updatedRecords == 0) {
                        throw new ConflictException("Stock was modified by another transaction. Please try again.");
                    }
                }
                case STOCK_ID -> {
                    if (!repository.existsById(value)) {
                        throw new NotFoundException("Unable to find product with id " + value);
                    }
                    var updatedRecords = repository.updateStockQuantityByStockId(value, version, quantity);
                    if (updatedRecords == 0) {
                        throw new ConflictException("Stock was modified by another transaction. Please try again.");
                    }
                }
                default ->
                        throw new WrongParameterException("Invalid by value: " + by + ".Use 'productcode' or 'stockid'.");
            }
        } catch (ConflictException | WrongParameterException | NotFoundException ce) {
            throw ce; // rethrow as-is
        } catch (Exception e) {
            throw new UpdateException("Unable to update quantity for : " + by + " " + value + " " + e);
        }
    }

    @Override
    @Transactional
    public void deleteStock(String by, String value) {
        try {
            switch (by.toLowerCase()) {
                case PRODUCT_CODE -> {
                    if (!repository.existsByProductCode(value)) {
                        throw new NotFoundException("Unable to find product  " + value);
                    }
                    repository.deleteByProductCode(value);
                    log.info("Stock Record with code: {} deleted ", value);
                }
                case STOCK_ID -> {
                    if (!repository.existsById(value)) {
                        throw new NotFoundException("Unable to find product with id " + value);
                    }
                    repository.deleteById(value);
                    log.info("Stock Record with id: {} deleted ", value);
                }
                default ->
                        throw new WrongParameterException("Invalid by value: " + by + ".Use 'productcode' or 'stockid'.");
            }
        } catch (WrongParameterException | NotFoundException ce) {
            throw ce; // rethrow as-is
        } catch (Exception e) {
            throw new DeletionException("Unable to delete stock for : " + by + " " + value);
        }
    }

    @Override
    @Transactional
    public void modifyStockQuantity(String by, String value, int quantity, int version) {
        try {
            switch (by.toLowerCase()) {
                case PRODUCT_CODE -> {
                    var stock = findByProductCode(value);
                    if (stock == null) {
                        throw new NotFoundException("Unable to find product " + value);
                    }
                    var updatedRecords = repository.updateStockQuantityByProductCode(value, version, getNewQuantity(stock, quantity));
                    if (updatedRecords == 0) {
                        throw new ConflictException("Stock was modified by another transaction. Please try again.");
                    }
                }
                case STOCK_ID -> {
                    var stock = findById(value);
                    if (stock == null) {
                        throw new NotFoundException("Unable to find product " + value);
                    }
                    var updatedRecords = repository.updateStockQuantityByStockId(value, version, getNewQuantity(stock, quantity));
                    if (updatedRecords == 0) {
                        throw new ConflictException("Stock was modified by another transaction. Please try again.");
                    }
                }
                default ->
                        throw new WrongParameterException("Invalid by value: " + by + ".Use 'productcode' or 'stockid'.");
            }
        } catch (ConflictException | WrongParameterException | NotFoundException | InsufficientStockException ce) {
            throw ce; // rethrow as-is
        } catch (Exception e) {
            throw new UpdateException("Unable to update quantiy for : " + by + " " + value + " " + e);
        }
    }

    @Override
    public boolean checkProductCodeExists(String productCode) {
        return repository.existsByProductCode(productCode);
    }

    private int getNewQuantity(Stock stock, int quantity) {
        var newQuantity = stock.getQuantity() + quantity;
        if (newQuantity >= 0) {
            return newQuantity;
        }
        throw new InsufficientStockException("Cannot reduce stock below zero. Current quantity: " + stock.getQuantity() + ", requested reduction: " + Math.abs(quantity));
    }
}
