package com.example.damagochibe.battle.dto.request;

import com.example.damagochibe.code.MessageType;
import lombok.Data;

@Data
public class BattleMessageReqDto {
    private MessageType type;
    private Long mongId;
    private String mongCode;
    private Integer latitude;
    private Integer longitude;
    private Integer battleRoomId;
    private String order;
}
