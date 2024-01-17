package com.example.damagochibe.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
@Entity
@Table(schema = "damagochi")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private String playerId;
    private String cartItemName;
    private Integer cartItemPrice;
    private Long itemCount;

}