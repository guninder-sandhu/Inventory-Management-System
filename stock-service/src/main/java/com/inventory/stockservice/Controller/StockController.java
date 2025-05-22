package com.inventory.stockservice.Controller;

import com.inventory.stockservice.entities.Stock;
import com.inventory.stockservice.exceptions.CreationException;
import com.inventory.stockservice.exceptions.RetrievalException;
import com.inventory.stockservice.repsonse.ApiResponse;
import com.inventory.stockservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StockController is a REST controller that manages operations related to stock management.
 * It provides endpoints for retrieving, creating, updating, and deleting stock data, along
 * with additional functionality for filtering and modifying stock based on specified criteria.
 * <p>
 * Endpoints:
 * - `/stocks`: Retrieves all stocks.
 * - `/stocks/{productCode}`: Retrieves stock details by product code.
 * - `/stocks/filter`: Filters stock records based on quantity comparison type and value.
 * - `/stocks/update-quantity`: Updates the stock quantity for a product or stock record.
 * - `/stocks/modify`: Modifies the stock quantity by adding or subtracting a value.
 * - `/stocks/delete`: Deletes stock records using product code or stock ID.
 * <p>
 * This controller interacts with the StockService layer to perform business logic and database operations.
 */
@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Stock>>> getStocks() {
        try {
            var stocks = service.findAll();
            ApiResponse<List<Stock>> response = new ApiResponse<>(
                    "Stocks retrieved successfully",
                    HttpStatus.OK.value(),
                    LocalDateTime.now(),
                    stocks);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve stocks at this time");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Integer>> getTotalStocks() {
        try {
            var stockQuantity = service.getAllStockQuantity();
            ApiResponse<Integer> response = new ApiResponse<>(
                    "Stocks Quantity retrieved successfully",
                    HttpStatus.OK.value(),
                    LocalDateTime.now(),
                    stockQuantity);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve stocks at this time");
        }
    }

    /**
     * Retrieves stock details based on the provided product code.
     *
     * @param productCode the unique code of the product whose stock details are to be retrieved
     * @return ResponseEntity containing an ApiResponse object with the stock details and status information
     * @throws RetrievalException if there is an error retrieving the stock
     */
    @GetMapping("/{productCode}")
    public ResponseEntity<ApiResponse<Stock>> getStockByCode(@PathVariable String productCode) {
        try {
            ApiResponse<Stock> response = new ApiResponse<>(
                    "Stock retrieved successfully",
                    HttpStatus.OK.value(),
                    LocalDateTime.now(),
                    service.findByProductCode(productCode));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve stocks at this time");
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Stock>> createStock(@RequestBody Stock stock) {
        try {
            ApiResponse<Stock> response = new ApiResponse<>(
                    "Stock created successfully",
                    HttpStatus.CREATED.value(),
                    LocalDateTime.now(),
                    service.createStock(stock));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CreationException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationException("Unable to create stock " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filter stock by quantity",
            description = "Returns stock based on comparison type (above, below, equal) and quantity value"
    )
    public ResponseEntity<ApiResponse<List<Stock>>> filterStockByQuantity(
            @Parameter(
                    description = "Type of comparison: above, below, equal",
                    example = "above"
            )
            @RequestParam String type,
            @Parameter(
                    description = "Quantity value to compare against",
                    example = "100"
            )
            @RequestParam int quantity) {
        ApiResponse<List<Stock>> response = new ApiResponse<>(
                "Stock created successfully",
                HttpStatus.CREATED.value(),
                LocalDateTime.now(),
                service.filterStockByQuantity(type, quantity));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-quantity")
    @Operation(
            summary = "Update quantity of product",
            description = "Updates quantity of product using ProductCode or StockId and quantity value." +
                    "NOTE: This overrides the current value"
    )
    public ResponseEntity<String> updateStockQuantity(
            @Parameter(
                    description = "Update By : productCode or stockId",
                    example = "productCode"
            ) @RequestParam String by,
            @Parameter(
                    description = "Values of ProductCode or stockId(UUID) to be updated",
                    example = "PRD0001"
            )
            @RequestParam String value,
            @Parameter(
                    description = "Quantity in integer which need to be stored in db",
                    example = "10"
            ) @RequestParam int quantity,
            @Parameter(
                    description = "version of record stored in db",
                    example = "1"
            ) @RequestParam int version) {
        service.updateStockQuantity(by, value, quantity, version);
        return ResponseEntity.ok("Stock quantity updated successfully.");
    }

    @PostMapping("/modify")
    @Operation(
            summary = "Modify quantity of product",
            description = "Modifies quantity of product using ProductCode or StockId and quantity value." +
                    "NOTE: This adds value to existing value."
    )
    public ResponseEntity<String> modifyStockQuantity(
            @Parameter(
                    description = "Modify By : productCode or stockId",
                    example = "productCode"
            ) @RequestParam String by,
            @Parameter(
                    description = "Values of ProductCode or stockId(UUID) to be updated",
                    example = "PRD0001"
            )
            @RequestParam String value,
            @Parameter(
                    description = "Quantity in integer which need to be added in existing quantity in db." +
                            "For subtracting give negative number",
                    example = "-10"
            ) @RequestParam int quantity,
            @Parameter(
                    description = "version of record stored in db",
                    example = "1"
            ) @RequestParam int version) {
        service.modifyStockQuantity(by, value, quantity, version);
        return ResponseEntity.ok("Stock quantity updated successfully.");
    }

    @PostMapping("/delete")
    @Operation(
            summary = "Delete stock of product",
            description = "Delete stock of product using ProductCode or StockId "
    )
    public ResponseEntity<Map<String, Object>> deleteStock(
            @Parameter(
                    description = "Update By : productCode or stockId",
                    example = "productCode"
            )
            @RequestParam String by,
            @Parameter(
                    description = "Values of ProductCode or stockId(UUID) to be updated",
                    example = "PRD0001"
            )
            @RequestParam String value) {
        service.deleteStock(by, value);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Stock deleted successfully.");
        response.put("status", HttpStatus.OK.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/listStocks")
    public ResponseEntity<ApiResponse<List<Stock>>> getStockByProductCodeList(
            @RequestParam List<String> productCodes) {

        List<Stock> stocks = service.findAllByProductCodeIn(productCodes);

        ApiResponse<List<Stock>> response = new ApiResponse<>(
                "Stocks retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                stocks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
