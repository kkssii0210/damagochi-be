package com.example.damagochibe.monginfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongDto {
    private Long mongId;
    private String name; //이름
    private String mongCode; //mong 코드
}
