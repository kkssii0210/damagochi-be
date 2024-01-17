package com.example.damagochibe.battle.service;

import com.example.damagochibe.battle.dto.response.BattleMessageResDto;

import com.example.damagochibe.battle.vo.BattleLog;
import com.example.damagochibe.battle.vo.BattleLog.FightType;
import com.example.damagochibe.battle.vo.MongStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

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
        log.info("keepMoney Call : mongId - {}", mongId);
        // point history 에 0원이라고 기록
//        AddPointReqDto addPointReqDto = AddPointReqDto.builder()
//                .point(0)
//                .content("배틀")
//                .code("AT012")
//                .build();
//        memberServiceClient.addPoint(String.valueOf(findMongMasterResDto.getMemberId()),
//                addPointReqDto);
    }

}
