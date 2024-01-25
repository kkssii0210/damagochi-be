package com.example.damagochibe.Item.food.dto;

import lombok.Data;

@Data
public class FoodDto {
    private Long storeId;
    private Long memberId;

    private String itemCategory;
    private Long foodId;
    private String foodName;
    private String foodFunction;
    private Integer foodPrice;
    private String fileUrl;
}
