package com.example.damagochibe.auth.controller;

import com.example.damagochibe.auth.dto.request.MemberAuthDto;
import com.example.damagochibe.auth.dto.response.AuthLoginResDto;
import com.example.damagochibe.auth.dto.response.ReissueResDto;
import com.example.damagochibe.auth.security.CustomUserDetailService;
import com.example.damagochibe.auth.security.TokenProvider;
import com.example.damagochibe.auth.service.AuthService;
import com.example.damagochibe.code.ErrorStateCode;
import com.example.damagochibe.exception.ForbiddenException;
import com.example.damagochibe.exception.NotFoundException;
import com.example.damagochibe.exception.TokenInvalidException;
import com.example.damagochibe.exception.TokenUnauthException;
import com.example.damagochibe.member.dto.request.LoginReqDto;
import com.example.damagochibe.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginReqDto loginRequestDto) {
        log.info("login - Call");
        try {
            AuthLoginResDto loginResDto = authService.login(loginRequestDto);
            log.info("code : {}, message : {}", ErrorStateCode.SUCCESS.getCode(),
                    ErrorStateCode.SUCCESS.name());
            return ResponseEntity.ok().body(loginResDto);
        } catch (NotFoundException e) {
            log.info("code : {}, message : {}", ErrorStateCode.GATEWAY.getCode(),
                    ErrorStateCode.GATEWAY.name());
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorStateCode.GATEWAY));
        } catch (TimeoutException e){
            log.info("code : {}, message : {}", ErrorStateCode.REDIS.getCode(),
                    ErrorStateCode.REDIS.name());
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorStateCode.REDIS));
        } catch (RuntimeException e) {
            log.info("code : {}, message : {}", ErrorStateCode.RUNTIME.getCode(),
                    ErrorStateCode.RUNTIME.name());
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorStateCode.RUNTIME));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization")String refreshToken){
        if(StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")){
            refreshToken = refreshToken.substring(7);
        }

        if(refreshToken != null){
            String id = tokenProvider.deleteRefreshToken(refreshToken);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/accessToken")
    public MemberAuthDto isTokenValid(@RequestHeader("Authorization")String accessToken){
        log.info("accessToken call");
        if(StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")){
            accessToken = accessToken.substring(7);
        }
        if(tokenProvider.validateToken(accessToken, customUserDetailService.loadUserByUsername(tokenProvider.getUsername(accessToken)))){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            MemberAuthDto dto = new MemberAuthDto(authentication.getName(),authentication.getAuthorities().stream().toList().get(0).toString());

            return dto;
        }
        // 유효하지 않은 토큰이거나 다른 이유로 인증에 실패한 경우
        // 캐시에 저장하지 않도록 null을 반환하거나 다른 방식으로 처리할 수 있습니다.
        return null;
    }

    @PostMapping("/reissue")
    private ResponseEntity<Object> reissue(
            @RequestHeader(value = "RefreshToken", required = false) String refreshToken) {
        log.info("reissue - Call");

        try {
            log.info("code : {}, message : {}", ErrorStateCode.SUCCESS.getCode(),
                    ErrorStateCode.SUCCESS.name());
            ReissueResDto reissueResDto = authService.reissue(refreshToken);
            return ResponseEntity.ok().body(reissueResDto);
        } catch (ForbiddenException e) {
            log.info("code : {}, message : {}", ErrorStateCode.REFRESH_TOKEN_EXPIRE.getCode(),
                    ErrorStateCode.REFRESH_TOKEN_EXPIRE.name());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(ErrorStateCode.REFRESH_TOKEN_EXPIRE));
        }catch (TokenInvalidException e){
            log.info("code : {}, message : {}", ErrorStateCode.TOKEN_INVALID.getCode(),ErrorStateCode.TOKEN_INVALID.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorStateCode.TOKEN_INVALID));
        }catch (TokenUnauthException e){
            log.info("code : {}, message : {}", ErrorStateCode.TOKEN_UNAUTH.getCode(),ErrorStateCode.TOKEN_UNAUTH.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorStateCode.TOKEN_UNAUTH));
        }catch (TimeoutException e) {
            log.info("code : {}, message : {}", ErrorStateCode.REDIS.getCode(),
                    ErrorStateCode.REDIS.name());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorStateCode.REDIS));
        } catch (RuntimeException e) {
            log.info("code : {}, message : {}", ErrorStateCode.RUNTIME.getCode(),
                    ErrorStateCode.RUNTIME.name());
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorStateCode.RUNTIME));
        }
    }
    @GetMapping("/isSocialMember")
    public Boolean isSocialMember(@RequestHeader("Authorization")String refreshToken) {
        if(StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")){
            refreshToken = refreshToken.substring(7);
        }

        if(refreshToken != null) {
            Boolean isSocial = tokenProvider.isSocialMember(refreshToken);
            System.out.println("isSocial = " + isSocial);
            return isSocial;
        }

        return false;
    }
}
