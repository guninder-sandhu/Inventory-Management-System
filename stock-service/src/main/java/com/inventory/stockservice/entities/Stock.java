package com.inventory.stockservice.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Stock {

    @Id
    @Column(name = "stockid")
    private String stockId;

    @Column(name = "productcode", unique = true, nullable = false)
    private String productCode;

    @Column(name = "quantity")
    private int quantity;

    /*To handle concurrency using Optimistic Locking
    This tells JPA: “Don’t let two updates overwrite each other silently.” If version has changed since it was read, an exception is thrown.
    Spring will then throw OptimisticLockingFailureException*/
    @Version
    private int version;

}
