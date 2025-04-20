package com.inventory.stockservice.service;

import com.inventory.stockservice.entities.Stock;

import java.util.List;


public interface StockService {

    Stock findByProductCode(String productCode);

    Stock createStock(Stock stock);

    Stock findById(String id);

    List<Stock> findAll();

    List<Stock> filterStockByQuantity(String type, int quantity);

    void updateStockQuantity(String by, String value, int quantity, int version);

    void deleteStock(String by, String value);

    void modifyStockQuantity(String by, String value, int quantity, int version);

    boolean checkProductCodeExists(String productCode);
}
