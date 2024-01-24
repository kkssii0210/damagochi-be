package com.example.damagochibe.itemFile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemFileDto {
    private Long id;

    private Long storeId;
    private String category;
    private String fileName;
    private String fileUrl;
}
