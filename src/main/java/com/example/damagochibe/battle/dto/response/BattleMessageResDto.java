package com.example.damagochibe.battle.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BattleMessageResDto {
    private String battleRoomId;
    private String mongCodeA;
    private String mongCodeB;
    private Integer nowTurn;
    private Integer totalTurn;
    private String nextAttacker;
    private String order;
    private Integer damageA;
    private Integer damageB;
    private Integer healthA;
    private Integer healthB;
}
