package com.example.damagochibe.auth.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SocialUserDto {
    public String playerId;
    public String password;
    public Integer point;
    public boolean isSocialMember;
}
