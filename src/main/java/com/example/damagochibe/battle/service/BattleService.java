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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    public BattleRoom findBattleRoomBySessionId(String sessionId) {
        for (BattleRoom room : battleRooms.values()) {
            if (room.getSessionIds().containsValue(sessionId)) {
                return room;
            }
        }
        return null;
    }
    // battleRoomId로 BattleRoom을 찾고 없으면 예외를 던지는 메소드
//    public BattleRoom findBattleRoomByIdOrThrow(Integer battleRoomId) {
//        BattleRoom battleRoom = battleRooms.get(battleRoomId);
//        if (battleRoom == null) {
//            throw new IllegalArgumentException("BattleRoom with ID " + battleRoomId + " not found.");
//        }
//        return battleRoom;
//    }

    public void updateBattleRooms(String sessionId) {
        BattleRoom room = findBattleRoomBySessionId(sessionId);

        BattleMessageResDto battleMessageResDto = BattleMessageResDto.builder()
                .battleRoomId(room.getBattleRoomId())
                .mongAId(room.getStatsMap().get("A") != null ? room.getStatsMap().get("A").getMongId() : null)
                .mongBId(room.getStatsMap().get("B") != null ? room.getStatsMap().get("B").getMongId() : null)
                .nowTurn(room.getNowTurn())
                .totalTurn(room.getTotalTurn())
                .sessionIds(room.getSessionIds())
                .build();
        log.info("보낼 세션!!! " + sessionId);
        messagingTemplate.convertAndSend(
                "/topic/battleRoom/" + room.getBattleRoomId(),
                battleMessageResDto // 현재 방의 상태 데이터
        );
        log.info("Updated battle rooms sent to respective participants");
    }


    public synchronized BattleRoom joinOrCreateRoom(String sessionId, Long mongId) {
        log.info("battleRoom 생성 Call");
        log.info("받아온 mongId :" + mongId);
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
                room.addSession(sessionId, stats);
                updateBattleRooms(sessionId);
                return room;
            }
        }
        // 새 방 생성
        int newRoomId = nextRoomId.getAndIncrement();
        BattleRoom newRoom = BattleRoom.builder()
                .totalTurn(10) // 예시 값
                .battleRoomId(newRoomId)
                .sessionIdA(null)
                .sessionIdB(null) // 초기에는 B 세션 없음
                .statsA(null) // stats 초기화
                .statsB(null) // 초기에는 B stats 없음
                .build();

        battleRooms.put(newRoomId, newRoom);
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

    public void getWin(Long mongId) {
        log.info("getWin Call : mongId - {}", mongId);
    }

    public void getLose(Long mongId) {
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

    public BattleMessageResDto attack(BattleMessageResDto resDto) {
        Optional<Mong> mongA = mongInfoRepo.findById(resDto.getMongAId());
        Optional<Mong> mongB = mongInfoRepo.findById(resDto.getMongBId());


        int damege = mongA.get().getStrength() * mongA.get().getAgility() / mongB.get().getDefense();
        double randomValue = 0.9 + (Math.random() * 0.2);

        if (mongA.get().getAttribute().equals("불")) {
            if (mongB.get().getAttribute().equals("풀")) {
                damege = (int) (damege * 1.2);
            } else if (mongB.get().getAttribute().equals("물")) {
                damege = (int) (damege * 0.8);
            }
        }

        if (mongA.get().getAttribute().equals("물")) {
            if (mongB.get().getAttribute().equals("불")) {
                damege = (int) (damege * 1.2);
            } else if (mongB.get().getAttribute().equals("풀")) {
                damege = (int) (damege * 0.8);
            }
        }

        if (mongA.get().getAttribute().equals("풀")) {
            if (mongB.get().getAttribute().equals("물")) {
                damege = (int) (damege * 1.2);
            } else if (mongB.get().getAttribute().equals("불")) {
                damege = (int) (damege * 0.8);
            }
        }

        if (mongA.get().getAttribute().equals("빛")) {
            if (mongB.get().getAttribute().equals("암")) {
                damege = (int) (damege * 1.2);
            }
        }

        if (mongA.get().getAttribute().equals("암")) {
            if (mongB.get().getAttribute().equals("빛")) {
                damege = (int) (damege * 1.2);
            }
        }

        int finalDamege = (int) (damege * randomValue);

        resDto.setTurn(mongB.get().getName());
        resDto.setHealthA(resDto.getHealthA());
        resDto.setHealthB(resDto.getHealthB() - finalDamege);
        System.out.println("finalDamege = " + finalDamege);
        return resDto;
    }
}
