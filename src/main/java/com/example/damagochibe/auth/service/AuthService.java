package com.example.damagochibe.auth.service;

import com.example.damagochibe.auth.dto.response.AuthLoginResDto;
import com.example.damagochibe.auth.dto.response.MemberLoginResDto;
import com.example.damagochibe.auth.dto.response.ReissueResDto;
import com.example.damagochibe.auth.entity.Auth;
import com.example.damagochibe.auth.entity.RefreshToken;
import com.example.damagochibe.auth.repository.AuthRepository;
import com.example.damagochibe.auth.repository.RefreshTokenRepository;
import com.example.damagochibe.auth.security.TokenInfo;
import com.example.damagochibe.auth.security.TokenProvider;
import com.example.damagochibe.code.JwtStateCode;
import com.example.damagochibe.exception.ForbiddenException;
import com.example.damagochibe.exception.NotFoundException;
import com.example.damagochibe.exception.TokenInvalidException;
import com.example.damagochibe.exception.TokenUnauthException;
import com.example.damagochibe.member.controller.MemberController;
import com.example.damagochibe.member.dto.request.LoginReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberController memberController;
    @Transactional
    public AuthLoginResDto login(LoginReqDto loginReqDto) throws RuntimeException, TimeoutException {
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        loginReqDto.setPassword(password);
        //try-catch 잡기
        MemberLoginResDto memberLoginResDto;
        try {
            // loginReq to findByPlayIdReqDto 만들기
            ObjectMapper om = new ObjectMapper();
            memberLoginResDto = om.convertValue(
                    memberController.login(loginReqDto).getBody(), MemberLoginResDto.class);
            log.info("memberLoginResDto{}",memberLoginResDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NotFoundException();
        }

        // 토큰 발급 및 캐시 서버에 저장
        TokenInfo tokenInfo = provideToken(loginReqDto, memberLoginResDto);
        log.info("Tokeninfo {}",tokenInfo);

        // 역할 설정 TODO:역할이 이미 있으면 더이상 설정 안하게 바꿔야함
        Auth user = authRepository.save(
                Auth.builder().memberId(String.valueOf(memberLoginResDto.getMemberId())).role("USER")
                        .build());
        log.info("user {}",user.getMemberId());
        return AuthLoginResDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }
    public TokenInfo provideToken(LoginReqDto loginReqDto, MemberLoginResDto memberLoginResDto)
            throws RuntimeException, TimeoutException {

        String accessToken = tokenProvider.generateAccessToken(loginReqDto.getPlayerId());
        String refreshToken = tokenProvider.generateRefreshToken(loginReqDto.getPlayerId());

        // 캐시 서버에 token 저장
        try {
            refreshTokenRepository.save(RefreshToken.builder()
                    .id(loginReqDto.getPlayerId())
                    .memberId(String.valueOf(memberLoginResDto.getMemberId()))
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .expiration(JwtStateCode.REFRESH_TOKEN_EXPIRATION_PERIOD.getValue()).build());
        } catch (Exception e) {
            //redis 에러 처리
            log.info(e.getMessage());
            throw new TimeoutException();
        }

        return TokenInfo.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
    @Transactional
    public ReissueResDto reissue(String token) throws RuntimeException, TimeoutException {

        // refresh 토큰이 없거나 jwt 형식이 아닌 경우
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new TokenInvalidException();
        }

        String userName = tokenProvider.getUsername(token);
        String newAccessToken = tokenProvider.generateAccessToken(userName);

        // 캐시 서버에서 토큰을 찾을 수 없을 때 ( refreshToken 만료 )
        // 사용자가 로그인을 다시 해야함
        RefreshToken refreshToken = refreshTokenRepository.findById(userName)
                .orElseThrow(() -> new ForbiddenException());

        // 리프레쉬 토큰 불일치
        if(!refreshToken.getRefreshToken().equals(token))throw new TokenUnauthException();


        RefreshToken newRefreshToken =
                RefreshToken.builder()
                        .id(refreshToken.getId())
                        .memberId(refreshToken.getMemberId())
                        .refreshToken(refreshToken.getRefreshToken())
                        .accessToken(newAccessToken)
                        .expiration(JwtStateCode.REFRESH_TOKEN_EXPIRATION_PERIOD.getValue())
                        .build();

        // 캐시 서버에 token 저장
        try {
            refreshTokenRepository.save(newRefreshToken);
        } catch (Exception e) {
            //redis 에러 처리
            log.info(e.getMessage());
            throw new TimeoutException();
        }
        return ReissueResDto.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
