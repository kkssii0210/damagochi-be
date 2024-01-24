package com.example.damagochibe.battle.controller;
import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.battle.dto.response.BattleMessageResDto;
import com.example.damagochibe.battle.service.BattleService;
import com.example.damagochibe.battle.vo.BattleRoom;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.monginfo.repository.MongInfoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BattleController {
    private final BattleService battleService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthConfig authConfig;
    private final MongInfoRepo mongInfoRepo;

    @MessageMapping("/createBattleRoom")
    public void createBattleRoom(SimpMessageHeaderAccessor headerAccessor){
        log.info("createBattleRoom Call");
        // 세션 ID 추출
        String sessionId = headerAccessor.getSessionId();
        // accessToken 검증
        String accessToken = headerAccessor.getFirstNativeHeader("accessToken");
        log.info("sessionId!!!! : " + sessionId);
        log.info("accessToken!!!! : " + accessToken);
        Member member = authConfig.tokenValidationServiceV1(accessToken);
        String memberId = member.getPlayerId();
        log.info("memberId!!!! : " + memberId);
        Long mongId = mongInfoRepo.findMongByPlayerId(memberId);
        log.info("mongId!!! : " + mongId);
        BattleRoom battleRoom = battleService.joinOrCreateRoom(sessionId,mongId);
        BattleMessageResDto response = BattleMessageResDto.builder()
                .battleRoomId(battleRoom.getBattleRoomId())
                .build();
        // 모든 구독자에게 새로운 BattleRoom 정보 전송
        messagingTemplate.convertAndSend("/topic/battleRooms", response);
    }
    @GetMapping("/api/battleRooms")
    public ResponseEntity<List<BattleRoom>> getBattleRooms() {
        // 현재 메모리에 저장된 배틀룸 목록 반환
        List<BattleRoom> rooms = battleService.getBattleRooms();
        log.info("rooms : "+rooms);
        return ResponseEntity.ok(rooms);
    }
}
