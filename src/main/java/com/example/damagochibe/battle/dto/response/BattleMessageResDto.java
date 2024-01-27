package com.example.damagochibe.battle.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BattleMessageResDto {
    private Integer battleRoomId;
    private String mongCodeA;
    private String mongCodeB;
    private Long mongAId;
    private Long mongBId;
    private Integer nowTurn;
    private Integer totalTurn;
    private String nextAttacker;
    private String order;
    private Integer damageA;
    private Integer damageB;
    private Integer healthA;
    private Integer healthB;
    private String turn;
    private Long itemId;
    private Double attackBuff;
}
