package com.example.damagochibe.auth.security;

import com.example.damagochibe.auth.repository.RefreshTokenRepository;
import com.example.damagochibe.code.JwtStateCode;
import com.example.damagochibe.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    @Value("${jwt.token.key}")
    private String JWT_KEY;

    public Claims extractAllClaims(String token) { // 2
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(JWT_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }


    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateAccessToken(String username) {
        return doGenerateToken(username,
                JwtStateCode.ACCESS_TOKEN_EXPIRATION_PERIOD.getValue());
    }

    public String generateRefreshToken(String username) {
        return doGenerateToken(username,
                JwtStateCode.REFRESH_TOKEN_EXPIRATION_PERIOD.getValue());
    }

    private String doGenerateToken(String username,  long expireTime) { // 1
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(JWT_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsername(token);
        return username.equals(userDetails.getUsername())
               && !isTokenExpired(token);
    }

    public long getRemainMilliSeconds(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }
    // 소셜 멤버인지 아닌지 논리값 리턴
    public Boolean isSocialMember(String refreshToken) {
        Long memberId = refreshTokenRepository.findMemberIdByToken(refreshToken);
        Boolean isSocialMember = memberRepository.checkSocialMemberByMemberId(memberId);
        return isSocialMember != null ? isSocialMember : false; //NullPointerException 나면 여기임
    }

    public String deleteRefreshToken(String refreshToken) {
        Long memberId = refreshTokenRepository.findMemberIdByToken(refreshToken);
        System.out.println("memberId = "+memberId);
        String id = null;
        if (isSocialMember(refreshToken)){
            id = memberRepository.findPlayerIdByMemberId(memberId);
        }
        if (memberId != null){
            refreshTokenRepository.deleteByMemberId(memberId);
        }
        return id;
    }
}
