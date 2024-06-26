package ru.lebruce.store.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shopping_cart_items", indexes = {
        @Index(name = "idx_shoppingCartItem_product", columnList = "product_id"),
        @Index(name = "idx_shoppingCartItem_shoppingCart", columnList = "shopping_cart_id"),
        @Index(name = "idx_shoppingCartItem_productSize", columnList = "product_size_id")
})
public class ShoppingCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartItemId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    @JsonIgnore
    private ShoppingCart shoppingCart;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_size_id")
    private ProductSize size;

    @Formula("(select p.price from products p where p.product_id = product_id)")
    private double priceForOne;

    @Formula("(select p.price * quantity from products p where p.product_id = product_id)")
    private double totalPrice;

}
