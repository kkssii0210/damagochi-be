package com.example.damagochibe.battle.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

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
    // 세션 ID를 위한 필드를 추가
    private Map<String, String> sessionIds;

    private String battleLog;
}
