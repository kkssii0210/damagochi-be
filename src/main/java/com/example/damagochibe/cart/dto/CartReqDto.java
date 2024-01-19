package com.example.damagochibe.cart.dto;

import lombok.Data;

@Data
public class CartReqDto {
    private String playerId;
    private Long storeId;
    private String category;
}
