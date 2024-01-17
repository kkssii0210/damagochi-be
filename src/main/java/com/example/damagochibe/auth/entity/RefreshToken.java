package com.example.damagochibe.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Getter
@Setter
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(nullable = false, updatable = false)
    private String id;
    private Long memberId;
    private String refreshToken;
    private String accessToken;
    private Date expiration;
    private Date refreshTokenExpiresIn;

}

