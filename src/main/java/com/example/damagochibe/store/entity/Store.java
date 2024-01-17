package com.example.damagochibe.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
