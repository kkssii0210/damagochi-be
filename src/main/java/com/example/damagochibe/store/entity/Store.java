package com.example.damagochibe.store.entity;

import com.example.damagochibe.itemFile.entity.ItemFile;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany(mappedBy = "store")
//    @JsonManagedReference
//    private List<ItemFile> itemFiles = new ArrayList<>();

    private Long memberId;
}
