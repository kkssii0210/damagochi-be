package com.example.damagochibe.battle.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

@Data
@Builder
public class Matching {
    private Long mongId;
    private String mongCode;
    //    private WebSocketSession session;
    private String sessionId; // WebSocketSession 대신 세션 ID 사용
    private LocalDateTime matchingStart;
}
