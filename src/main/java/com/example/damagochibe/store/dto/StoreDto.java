package com.example.damagochibe.store.dto;

import com.example.damagochibe.itemFile.dto.ItemFileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @NotNull
    private String itemCode;

    private List<ItemFileDto> itemFiles;
}
