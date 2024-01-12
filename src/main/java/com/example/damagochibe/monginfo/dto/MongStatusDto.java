package com.example.damagochibe.monginfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongStatusDto {
    private String mongCode;
    private String name;
    private boolean clean; //  청소
    private Integer evolutionLevel; //   진화 레벨
    private Integer level; //레벨
    private Integer health; //  체력
    private Integer feed;   //배고픔
    private Integer tired; //피로도
}