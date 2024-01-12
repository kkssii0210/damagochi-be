package com.example.damagochibe.Item.mapBackground.background.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapDto {
    private Long mapId;
    private Long StoreId;
    private String itemCategory;
    private String mapName;
    private String mapFunction;
    private Integer mapPrice;
}
