package com.example.damagochibe.Item.mapBackground.background.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DynamicInsert
@Table(name = "mymap")
public class Mymap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mymap_id")
    private Long mymapId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "map_code")
    private String mapCode;
    @Column(name = "upd_dt")
    private LocalDateTime updDt;

    //상점에서 맵 판매를 위해 생성한 필드들
    @Column(name = "store_id")
    private Long storeId;
    private String mapName;
    private String category;
    private String mapFunction;
    private Integer mapPrice;
    private String fileUrl;
}
