package com.example.damagochibe.inventory.enetity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(schema = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private String memberId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String itemCode;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private Integer quantity;


}
