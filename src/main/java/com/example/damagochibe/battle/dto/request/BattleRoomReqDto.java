package com.example.damagochibe.battle.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BattleRoomReqDto {
    private Integer totalTurn;
    private String sessionId;
    private Long mongId;
}
