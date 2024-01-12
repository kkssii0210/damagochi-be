package com.example.damagochibe.Item.liquidMedicine.LiquidMedicineDto;

import lombok.Data;

@Data
public class LiquidMedicineDto {
    private Long liquidMedicineId;
    private Long storeId;
    private String liquidMedicineName;
    private String liquidMedicineFunction;
    private Integer liquidMedicinePrice;
    private Long memberId;
}
