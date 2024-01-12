package com.example.damagochibe.Store.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "damagochi")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    private String itemCategory;
    private String itemName;
    private String itemFunction;
    private Integer itemPrice;

    private Long memberId;
}
