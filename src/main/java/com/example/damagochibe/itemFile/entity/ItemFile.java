package com.example.damagochibe.itemFile.entity;


import com.example.damagochibe.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "damagochi")
public class ItemFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private Long storeId;
    private String category;
    private String fileName;
    private String fileUrl;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "storeId")
//    @JsonBackReference
//    @JsonIgnore
//    private Store store;
}
