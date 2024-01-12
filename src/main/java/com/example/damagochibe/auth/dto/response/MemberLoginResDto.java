package com.example.damagochibe.auth.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MemberLoginResDto {
    private Long memberId;
    private String mapCode;
    private String point;
}
