package com.example.damagochibe.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(nullable = false, updatable = false)
    private String id;
    private String memberId;
    private String refreshToken;
    private String accessToken;
    private Long expiration;
}

