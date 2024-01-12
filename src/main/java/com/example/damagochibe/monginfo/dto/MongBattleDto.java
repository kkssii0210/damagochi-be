package com.example.damagochibe.monginfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongBattleDto {
    private Long mongId;
    private Integer health; //  체력
    private Integer tired; //피로도
    private Integer defense; //  방어력
    private Integer strength; //  근력
    private Integer agility; //  민첩
    private boolean debuff;   //디버프
    private Integer win;   //우승 횟수
    private Integer lose;  //진 횟수
}


