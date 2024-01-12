package com.example.damagochibe.member.dto.response;

import com.example.damagochibe.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecureResDto {
    private Long memberId;
    private Integer point;
    private String password;
    private String playerId;

    public SecureResDto of(Member member)  {
        return SecureResDto.builder()
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .playerId(member.getPlayerId())
                .point(member.getPoint())
                .build();
    }
}
