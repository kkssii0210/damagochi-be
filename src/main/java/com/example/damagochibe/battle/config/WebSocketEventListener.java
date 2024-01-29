package com.example.damagochibe.battle.config;

import com.example.damagochibe.battle.service.BattleService;
import com.example.damagochibe.battle.vo.BattleRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {
    private final BattleService battleService;
    public WebSocketEventListener(BattleService battleService) {
        this.battleService = battleService;
    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        log.info("연결해제 감지!!!!");
        String sessionId = event.getSessionId();
        BattleRoom battleRoom = battleService.findBattleRoomBySessionId(sessionId);
        if (battleRoom != null) {
            battleRoom.removeSession(sessionId);
        }
    }
}
