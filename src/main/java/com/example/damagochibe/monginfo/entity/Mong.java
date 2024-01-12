package com.example.damagochibe.monginfo.entity;

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
public class Mong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mongId")
    private Long id; //-
    @Column
    private String mongCode;
    @Column
    private String memberId;
    @Column
    private String name;
    @Column(name = "birth", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime birth; //  캐릭터 생성 날짜
    @Column
    private Integer age;
    //    240104 stats : 근력과 민첩 | status : 배고픔(허기짐)
    @Column(columnDefinition = "INT DEFAULT 3")
    private Integer strength; //  근력
    @Column(columnDefinition = "INT DEFAULT 3")
    private Integer agility; //  민첩
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean debuff; //디버프
    @Column(columnDefinition = "INT DEFAULT 3")
    private Integer defense; //  방어력
    @Column(columnDefinition = "INT DEFAULT 100")
    private Integer health; //  체력
    @Column
    private String attribute; //  attribute는 게임 캐릭터의 속성을 의미함. 물, 불, 흙, 빛
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean clean; //  청소
    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer evolutionLevel; //   진화 레벨
    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer level; //레벨
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer win; //우승 횟수
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer lose; //
    @Column(columnDefinition = "INT DEFAULT 100")
    private Integer feed; //배고픔
    @Column( columnDefinition = "INT DEFAULT 100")
    private Integer tired; // 피로도
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer exp; // 경험차



}

