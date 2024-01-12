package com.example.damagochibe.monginfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongInfoDto {
    private Long mongId;
    private String name;
    private Integer age;
    private LocalDateTime birth;
    private String mongCode;
    private String memberId;
    private String attribute;
    private Integer evolutionLevel;
    private Integer level;
// TODO 몽의 정보. 컨펌 ..아니고 알아서하기 .... age랑 memberId 있어야 하는가
}
