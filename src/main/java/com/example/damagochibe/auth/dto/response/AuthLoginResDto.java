package com.example.damagochibe.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginResDto {
    private String accessToken;
    private String refreshToken;
}
