package com.example.damagochibe.battle.service;

import com.example.damagochibe.battle.dto.response.BattleMessageResDto;

import com.example.damagochibe.battle.vo.BattleLog;
import com.example.damagochibe.battle.vo.BattleLog.FightType;
import com.example.damagochibe.battle.vo.BattleRoom;
import com.example.damagochibe.battle.vo.MongStats;
import com.example.damagochibe.monginfo.entity.Mong;
import com.example.damagochibe.monginfo.repository.MongInfoRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {
    private final ObjectMapper objectMapper;
    private final Map<Integer, BattleRoom> battleRooms = new ConcurrentHashMap<>();
    private final AtomicInteger nextRoomId = new AtomicInteger(1);
    private final MongInfoRepo mongInfoRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public List<BattleRoom> getBattleRooms() {
        return new ArrayList<>(battleRooms.values());
    }
    public void updateBattleRooms() {
        // 배틀룸 목록 또는 상태 변경 로직
        List<BattleRoom> currentBattleRooms = new ArrayList<>(battleRooms.values());
        // 변화된 배틀룸 목록을 클라이언트에게 전송
        messagingTemplate.convertAndSend("/topic/battleRooms", currentBattleRooms);
        log.info("Updated battle rooms sent");
    }
    public synchronized BattleRoom joinOrCreateRoom(String sessionId, Long mongId) {
        log.info("battleRoom 생성 Call");
        log.info("받아온 mongId :"+mongId);
        //mongId로 MongStats 불러와야함.
        Optional<Mong> byId = mongInfoRepo.findById(mongId);
        Mong mong = byId.get();
        // MongStats 인스턴스 생성 또는 조회
        MongStats stats = MongStats.builder()
                .mongId(mongId)
                .health(mong.getHealth())
                .attribute(mong.getAttribute())
                .defense(mong.getDefense())
                .strength(mong.getStrength())
                .agility(mong.getAgility())
                .build();

        // 가용한 방 찾기
        for (BattleRoom room : battleRooms.values()) {
            if (!room.isFull()) {
                room.addSession(sessionId,stats);
                updateBattleRooms();
                return room;
            }
        }
        // 새 방 생성
        int newRoomId = nextRoomId.getAndIncrement();
        BattleRoom newRoom = BattleRoom.builder()
                .totalTurn(10) // 예시 값
                .battleRoomId(newRoomId)
                .sessionIdA(sessionId)
                .sessionIdB(null) // 초기에는 B 세션 없음
                .statsA(stats) // stats 초기화
                .statsB(null) // 초기에는 B stats 없음
                .build();

        battleRooms.put(newRoomId, newRoom);
        updateBattleRooms();
        return newRoom;
    }

    public BattleMessageResDto battleActive(Integer nowTurn, MongStats statsA, MongStats statsB,
                                            BattleLog battleLog) {
        /*
            - 몸무게 = 방어력  ///  근력 = 공격
            - 서로의 피를 알 수 있음
            - 받는 데미지 : 상대 데미지 * 100/(100+나의 방어력)
            - 주는 데미지 : 100 + 나의 공격력
            - 최대 10번 턴 제
            - 체력 500
        */
        String nextAttacker;
        Integer damageA = 0;
        Integer damageB = 0;

        if (nowTurn % 2 != 0) {
            // A 공격

            // 1명이라도 STAY 한 경우
            if (isStay(battleLog)) {
                // B만 STAY 인 경우
                if (!battleLog.getSelectA().equals(FightType.STAY)) {
                    log.info("A 공격 : B만 STAY. 무조건 공격 성공");
                    damageB = attack(statsA, statsB);
                }
                // A만 STAY 이거나, A와 B 모두 STAY 인 경우
                else {
                    // B 수비 성공
                    log.info("A 공격 : A만 STAY 이거나, A와 B 모두 STAY");
                    damageA = 0;
                    damageB = 0;
                }
            } else {
                if (!isSameDirection(battleLog)) {
                    // B가 A의 공격을 받음
                    log.info("A 공격 : 다른방향");
                    damageB = attack(statsA, statsB);
                } else {
                    log.info("A 공격 : 같은방향");
                    // B가 A의 공격을 회피함
                    damageA = 0;
                    damageB = 0;
                }
            }

            nextAttacker = "B";

        } else {
            // B 공격

            // 1명이라도 STAY 한 경우
            if (isStay(battleLog)) {
                // A만 STAY 인 경우
                if (!battleLog.getSelectB().equals(FightType.STAY)) {
                    log.info("B 공격 : A만 STAY. 무조건 공격 성공");
                    damageA = attack(statsB, statsA);
                }
                // B만 STAY 이거나, A와 B 모두 STAY 인 경우
                else {
                    // A 수비 성공
                    log.info("B 공격 : B만 STAY 이거나, A와 B 모두 STAY");
                    damageA = 0;
                    damageB = 0;
                }
            } else {

                if (!isSameDirection(battleLog)) {
                    // A가 B의 공격을 받음
                    log.info("B 공격 : 다른방향");
                    damageA = attack(statsB, statsA);
                } else {
                    // A가 B의 공격을 회피함
                    log.info("B 공격 : 같은방향");
                    damageA = 0;
                    damageB = 0;
                }

            }

            nextAttacker = "A";

        }
        return BattleMessageResDto.builder()
                .nextAttacker(nextAttacker)
                .damageA(damageA)
                .damageB(damageB)
                .build();
    }
    private boolean isStay(BattleLog battleLog) {
        if (battleLog.getSelectA().equals(FightType.STAY) || battleLog.getSelectB()
                .equals(FightType.STAY)) {
            return true;
        } else {
            return false;
        }
    }
    private Integer attack(MongStats attackMong, MongStats defenceMong) {
        //TODO:공격력 로직.. 추후 수정 필요
        Integer power = 100 + attackMong.getStrength();
        Integer defense = 100 + defenceMong.getDefense();
        return power * 100 / defense;
    }
    private boolean isSameDirection(BattleLog battleLog) {
        if (battleLog.getSelectA().equals(battleLog.getSelectB())) {
            return true;
        } else {
            return false;
        }
    }
    public void keepWin(Long mongId) {
        log.info("keepWin Call : mongId - {}", mongId);
        // point history 에 0원이라고 기록
//        AddPointReqDto addPointReqDto = AddPointReqDto.builder()
//                .point(0)
//                .content("배틀")
//                .code("AT012")
//                .build();
//        memberServiceClient.addPoint(String.valueOf(findMongMasterResDto.getMemberId()),
//                addPointReqDto);
    }
    public void getWin(Long mongId){
        log.info("getWin Call : mongId - {}", mongId);
    }
    public void getLose(Long mongId){
        log.info("getLose Call : mongId - {}", mongId);
    }
    public <T> void sendMessage(String userSessionId, String destination, T message, SimpMessagingTemplate messagingTemplate) {
        //destination은 메세지를 보낼 목적지
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            messagingTemplate.convertAndSendToUser(userSessionId, destination, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("메시지 변환 오류", e);
        }
    }
}
