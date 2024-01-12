package com.example.damagochibe.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberAuthDto {
    private String playerId;
    private String role;
}
