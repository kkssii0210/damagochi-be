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
}
