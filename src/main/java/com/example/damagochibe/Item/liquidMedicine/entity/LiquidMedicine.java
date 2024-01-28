package com.example.damagochibe.Item.liquidMedicine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
@Table(schema = "damagochi")
public class LiquidMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long liquidMedicineId;

    private Long storeId;
    private String liquidMedicineName;
    private String category;
    private String liquidMedicineFunction;
    private Integer liquidMedicinePrice;
    private String liquidMedicineCode;
    private String fileUrl;
}
