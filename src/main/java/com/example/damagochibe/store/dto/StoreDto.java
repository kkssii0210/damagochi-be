package com.example.damagochibe.store.dto;

import com.example.damagochibe.itemFile.entity.ItemFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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

    private List<ItemFile> itemFiles;
}
