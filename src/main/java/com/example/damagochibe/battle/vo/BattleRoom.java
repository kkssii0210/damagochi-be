package com.example.damagochibe.battle.vo;

import com.example.damagochibe.battle.dto.request.BattleMessageReqDto;
import com.example.damagochibe.battle.dto.response.BattleMessageResDto;
import com.example.damagochibe.battle.service.BattleService;
import com.example.damagochibe.code.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
public class BattleRoom {
    private Integer totalTurn;
    private Integer battleRoomId;
    private Map<String, String> sessionIds; // 사용자 ID 또는 세션 ID 저장
    private Map<String, MongStats> statsMap;
    private BattleLog battleLog;
    private Integer nowTurn = 0;
    @Builder
    public BattleRoom(Integer totalTurn, Integer battleRoomId, String sessionIdA, String sessionIdB, MongStats statsA, MongStats statsB) {
        this.totalTurn = totalTurn;
        this.battleRoomId = battleRoomId;
        this.sessionIds = new HashMap<>() {{
            put("A", sessionIdA);
            put("B", sessionIdB);
        }};
        this.statsMap = new HashMap<>() {{
            put("A", statsA);
            put("B", statsB);
        }};
        this.battleLog = new BattleLog();
        this.nowTurn = 1;
    }
    // A 가 먼저 공격 (A는 기본적으로 먼저 방만든 사람이 됨)
    public void handlerActions(BattleMessageReqDto battleMessageReqDto, BattleService battleService, SimpMessagingTemplate messagingTemplate)
            throws IOException, RuntimeException {

        String order = battleMessageReqDto.getOrder();

        log.info(nowTurn + " : " + order + " 선택완료");
        // LEFT, RIGHT, STAY 3가지 TYPE
        if (battleMessageReqDto.getType().equals(MessageType.LEFT)) {
            if (order.equals("A")) {
                battleLog.setSelectA(BattleLog.FightType.LEFT);
            } else {
                battleLog.setSelectB(BattleLog.FightType.LEFT);
            }
        } else if (battleMessageReqDto.getType().equals(MessageType.RIGHT)) {
            if (order.equals("A")) {
                battleLog.setSelectA(BattleLog.FightType.RIGHT);
            } else {
                battleLog.setSelectB(BattleLog.FightType.RIGHT);
            }
        } else {
            if (order.equals("A")) {
                battleLog.setSelectA(BattleLog.FightType.STAY);
            } else {
                battleLog.setSelectB(BattleLog.FightType.STAY);
            }
        }

        // 한턴이 끝났는지 확인
        Boolean isOver = false;
        if ((order.equals("A") && battleLog.getSelectB() != null) || (order.equals("B")
                                                                      && battleLog.getSelectA() != null)) {
            isOver = true;
        }

        // 한턴이 끝남
        if (isOver) {
            log.info("모두 선택완료 결과 계산");
            MongStats statsA = statsMap.get("A");
            MongStats statsB = statsMap.get("B");

            BattleMessageResDto battleMessageResDto
                    = battleService.battleActive(nowTurn, statsA, statsB, battleLog);

            Integer healthA = statsA.getHealth() - battleMessageResDto.getDamageA();
            Integer healthB = statsB.getHealth() - battleMessageResDto.getDamageB();

            // 응답 갱신
            battleMessageResDto.setBattleRoomId(battleMessageReqDto.getBattleRoomId());
            battleMessageResDto.setMongCodeA("");
            battleMessageResDto.setMongCodeB("");
            battleMessageResDto.setNowTurn(nowTurn);
            battleMessageResDto.setTotalTurn(totalTurn);
            battleMessageResDto.setHealthA(healthA);
            battleMessageResDto.setHealthB(healthB);

            // 턴 갱신, 현재 체력 갱신
            statsMap.get("A").setHealth(healthA);
            statsMap.get("B").setHealth(healthB);

            // 마지막 턴인지 확인
            if (totalTurn.equals(nowTurn) || healthA <= 0 || healthB <= 0) {
                // 승패 갱신 해야함

                // 1. 동점
                if (healthA == 0 && healthB == 0) {
                    battleService.keepWin(statsA.getMongId());
                    battleService.keepWin(statsB.getMongId());
                }
                // 2. A 승리
                else if (healthA > healthB) {
                    battleService.getWin(statsA.getMongId());
                    battleService.getLose(statsB.getMongId());
                }
                // 3. B 승리
                else if (healthB > healthA) {
                    battleService.getWin(statsA.getMongId());
                    battleService.getLose(statsB.getMongId());
                }

                battleMessageResDto.setNowTurn(-1);
                nowTurn = -1;
            } else {
                nowTurn++;
                battleLog.setSelectA(null);
                battleLog.setSelectB(null);
            }

            // 메시지 전송 부분 수정
            String sessionIdA = sessionIds.get("A");
            String sessionIdB = sessionIds.get("B");

            battleMessageResDto.setOrder("A");
            battleService.sendMessage(sessionIdA, "/queue/battle", battleMessageResDto, messagingTemplate);

            battleMessageResDto.setOrder("B");
            battleService.sendMessage(sessionIdB, "/queue/battle", battleMessageResDto, messagingTemplate);
        }
    }
    public boolean isFull() {
        long count = sessionIds.values().stream()
                .filter(Objects::nonNull)
                .count();
        return count >= 2; // 2명 이상이면 full로 간주
    }
    public void addSession(String sessionId, MongStats stats) {
        log.info("addSession Call!!!");
        if (!isFull()) {
            if (sessionIds.get("A") == null) {
                sessionIds.put("A", sessionId);
                // statsMap 및 기타 필요한 초기화 로직
                statsMap.put("A", stats);
            } else if (sessionIds.get("B") == null) {
                sessionIds.put("B", sessionId);
                // statsMap 및 기타 필요한 초기화 로직
                statsMap.put("B",stats);
            }
        } else {
            throw new IllegalStateException("BattleRoom is already full");
        }
    }
    public void removeSession(String sessionId) {
        boolean isRemoved = false;

        if (sessionId.equals(sessionIds.get("A"))) {
            sessionIds.put("A", null);
            statsMap.put("A", null);
            isRemoved = true;
        }

        if (sessionId.equals(sessionIds.get("B"))) {
            sessionIds.put("B", null);
            statsMap.put("B", null);
            isRemoved = true;
        }

        if (isRemoved) {
            log.info("Session removed: " + sessionId);
        } else {
            log.info("Session to remove not found: " + sessionId);
        }
    }
}
