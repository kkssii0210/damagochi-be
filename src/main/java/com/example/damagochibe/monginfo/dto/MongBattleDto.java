package com.example.damagochibe.monginfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongBattleDto {
    private Long mongId;
    private Integer win;   //우승 횟수
    private Integer lose;  //진 횟수
}


