package com.inventory.stockservice.repository;

import com.inventory.stockservice.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {

    @Query("select s from Stock s where s.quantity> :quantity")
    List<Stock> getProductStockAboveSomeQuantity(@Param("quantity") int quantity);

    @Query("select s from Stock s where s.quantity< :quantity")
    List<Stock> getProductStockBelowSomeQuantity(@Param("quantity") int quantity);

    @Query("select s from Stock s where s.quantity= :quantity")
    List<Stock> getProductStockEqualToQuantity(@Param("quantity") int quantity);

    Stock getStockByProductCode(String productCode);

    Stock getStocksByStockId(String stockId);

    boolean existsByProductCode(String productCode);

    @Modifying
    @Query("UPDATE Stock s SET s.quantity= :quantity, s.version = s.version + 1 WHERE s.productCode = :productCode AND s.version = :version")
    int updateStockQuantityByProductCode(@Param("productCode") String productCode, @Param("version") int version, @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE Stock s SET s.quantity= :quantity, s.version = s.version + 1 WHERE s.stockId = :stockId AND s.version = :version")
    int updateStockQuantityByStockId(@Param("stockId") String stockId, @Param("version") int version, @Param("quantity") int quantity);

    void deleteByProductCode(String value);
}
