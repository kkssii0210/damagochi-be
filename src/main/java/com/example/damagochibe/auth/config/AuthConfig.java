package com.example.damagochibe.auth.config;

import com.example.damagochibe.auth.repository.RefreshTokenRepository;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.repository.MemberRepository;
import com.example.damagochibe.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthConfig {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public Member tokenValidationService(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Incorrect or missing Authorization header");
        }
        String token = authorizationHeader.substring(7);
        Long memberIdByAccessToken = refreshTokenRepository.findMemberIdByAccessToken(token);
        Optional<Member> byMemberId = memberRepository.findByMemberId(memberIdByAccessToken);
        return byMemberId.orElse(null);
    }
}

