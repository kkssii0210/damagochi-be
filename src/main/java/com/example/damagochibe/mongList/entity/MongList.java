package com.example.damagochibe.mongList.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(schema = "damagochi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class MongList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id; //-
    @Column
    private String mongCode;
    @Column(columnDefinition = "INT DEFAULT 3")
    private Integer strength; //  근력
    @Column(columnDefinition = "INT DEFAULT 3")
    private Integer agility; //  민첩
    @Column(columnDefinition = "INT DEFAULT 3")
    private Integer defense; //  방어력
    @Column(columnDefinition = "INT DEFAULT 100")
    private Integer health; //  체력
    @Column
    private String attribute; //  attribute는 게임 캐릭터의 속성을 의미함. 물, 불, 흙, 빛
    @Column
    private String image;


}