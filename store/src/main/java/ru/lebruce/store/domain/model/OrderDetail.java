package ru.lebruce.store.domain.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderDetailStatus status;
}