package com.example.damagochibe.cart.dto;

import lombok.Data;

@Data
public class CartResDto {
    private String playerId;
    private String itemCategory;
    private String itemName;
    private Integer itemPrice;
}
