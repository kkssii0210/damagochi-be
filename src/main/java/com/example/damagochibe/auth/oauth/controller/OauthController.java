package com.example.damagochibe.auth.oauth.controller;
import com.example.damagochibe.auth.dto.response.AuthLoginResDto;
import com.example.damagochibe.auth.oauth.dto.SocialOauthToken;
import com.example.damagochibe.auth.oauth.service.OauthService;
import com.example.damagochibe.code.ErrorStateCode;
import com.example.damagochibe.exception.NotFoundException;
import com.example.damagochibe.member.dto.response.LoginResDto;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;


@RestController
@Slf4j
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("api/member/kakao")
    public ResponseEntity<Object> getMemberFromKakao(@RequestBody SocialOauthToken socialOauthToken) {
        log.info("socialLogin - Call");
        String accessToken = socialOauthToken.getAccess_token();
        log.info("access Token ");
        log.info(accessToken);
        oauthService.getKakaoMember(accessToken);
        oauthService.generateToken(socialOauthToken);
        try {
            AuthLoginResDto loginResDto = AuthLoginResDto.builder()
                    .accessToken(socialOauthToken.getAccess_token())
                    .refreshToken(socialOauthToken.getRefresh_token())
                    .build();
            log.info("code : {}, message : {}", ErrorStateCode.SUCCESS.getCode(),
                    ErrorStateCode.SUCCESS.name());
            return ResponseEntity.ok().body(loginResDto);
        }catch (NotFoundException e) {
            log.info("code : {}, message : {}", ErrorStateCode.GATEWAY.getCode(),
                    ErrorStateCode.GATEWAY.name());
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorStateCode.GATEWAY));
        } catch (RuntimeException e) {
            log.info("code : {}, message : {}", ErrorStateCode.RUNTIME.getCode(),
                    ErrorStateCode.RUNTIME.name());
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorStateCode.RUNTIME));
        }
    }
}
