package com.example.damagochibe.Item.food.entity;


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
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    private String foodName;
    private String foodCategory;
    private String foodFunction;
    private Integer foodPrice;
    private String foodCode;

    private Long storedId;
    private Long memberId;

}
