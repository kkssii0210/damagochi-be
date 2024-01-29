package com.example.damagochibe.cart.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(schema = "damagochi")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private String playerId;
    private String cartItemCategory;
    private String cartItemName;
    private Integer cartItemPrice;
    private Integer cartItemCount;
    private String cartItemCode;

}
