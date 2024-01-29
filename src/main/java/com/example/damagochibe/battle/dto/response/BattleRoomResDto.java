package com.example.damagochibe.battle.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
public class BattleRoomResDto {
    private Integer battleRoomId;
    private Integer nowTurn;
    private Integer totalTurn;
    private Map<String, String> sessionIds;

}
