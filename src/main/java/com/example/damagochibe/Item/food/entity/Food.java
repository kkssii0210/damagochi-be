package com.example.damagochibe.Item.food.entity;


import com.example.damagochibe.itemFile.entity.ItemFile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
@Table(schema = "damagochi")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    private String foodName;
    private String category;
    private String foodFunction;
    private Integer foodPrice;
    private String foodCode;
    private String fileUrl;

    private Long storeId;
    private Long memberId;

}
