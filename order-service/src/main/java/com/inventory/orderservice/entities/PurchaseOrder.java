package com.inventory.orderservice.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inventory.orderservice.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {

    @Id
    @Column(name = "order_Id")
    private String orderId;

    @Column(name = "order code", nullable = false, unique = true)
    private String orderCode;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonManagedReference
    private Vendor vendor;

    @Column(name = "order_cost", nullable = false)
    private Double orderCost;

    @Column(name = "created_by", nullable = false)
    private String createBy;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PurchaseOrderItems> purchaseOrderItems;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
