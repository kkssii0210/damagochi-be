package com.example.damagochibe.battle.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MongStats {
    private Long mongId;
    private Integer strength; //근력
    private Integer agility; //민첩
    private Integer defense; //방어력
    private Integer health; //체력
    private String attribute; //속성
}
