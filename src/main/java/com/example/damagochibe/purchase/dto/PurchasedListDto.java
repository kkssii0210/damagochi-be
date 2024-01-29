package com.example.damagochibe.purchase.dto;

import com.example.damagochibe.store.dto.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedListDto {
    private String itemName;
    private String itemCategory;
    private Integer itemCount;
    private String itemCode;
}
