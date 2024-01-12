package com.example.damagochibe.Store.dto;

import lombok.Data;

@Data
public class StoreDto {
    private Long storeId;
    private String itemCategory;
    private String itemName;
    private String itemFunction;
    private Integer itemPrice;
}
