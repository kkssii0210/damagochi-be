package com.example.damagochibe.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreDto {
    private Long storeId;
    @NotBlank
    private String itemCategory;
    @NotBlank
    private String itemName;
    @NotBlank
    private String itemFunction;
    @NotNull
    private Integer itemPrice;
}
