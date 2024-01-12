package com.example.damagochibe.auth.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo {
    private String accessToken;
    private String refreshToken;
}
