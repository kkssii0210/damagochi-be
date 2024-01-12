package com.example.damagochibe.member.controller;

import com.example.damagochibe.auth.security.TokenProvider;
import com.example.damagochibe.code.PaypointStateCode;
import com.example.damagochibe.exception.NotFoundException;
import com.example.damagochibe.member.dto.request.LoginReqDto;
import com.example.damagochibe.member.dto.request.ModifyPointReqDto;
import com.example.damagochibe.member.dto.request.SecureReqDto;
import com.example.damagochibe.member.dto.response.FindMemberInfoResDto;
import com.example.damagochibe.member.dto.response.LoginResDto;
import com.example.damagochibe.member.dto.response.ModifyPointResDto;
import com.example.damagochibe.member.service.MemberService;
import com.example.damagochibe.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    @GetMapping("/info")
    public ResponseEntity<Object> findMemberInfo(HttpServletRequest httpServletRequest) {
        log.info("findMemberInfo - Call");
//        Long memberId = Long.parseLong(httpServletRequest.getHeader(headerMember));
        try {
            // "Authorization" 헤더에서 토큰 추출
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Incorrect or missing Authorization header");
            }
            String token = authorizationHeader.substring(7); // "Bearer " 제거
            String username = tokenProvider.getUsername(token);
            Long memberId = memberService.findMemberId(username);
            FindMemberInfoResDto findMemberInfoResDto = memberService.findMemberInfo(memberId);
            return ResponseEntity.ok().body(findMemberInfoResDto);
        } catch (NotFoundException e) {
            log.error(PaypointStateCode.NOTEXIST.getMessage());
            return ResponseEntity.ok().body(new ErrorResponse(PaypointStateCode.NOTEXIST));
        } catch (RuntimeException e) {
            log.error(PaypointStateCode.UNKNOWN.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.UNKNOWN));
        }
    }

    @PutMapping("/paypoint")
    public ResponseEntity<Object> modifyPoint(@RequestBody ModifyPointReqDto modifyPointDto,
                                              HttpServletRequest httpServletRequest) {
        log.info("modifyPoint - Call");
        try {
            // "Authorization" 헤더에서 토큰 추출
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Incorrect or missing Authorization header");
            }
            String token = authorizationHeader.substring(7); // "Bearer " 제거
            String username = tokenProvider.getUsername(token);
            Long memberId = memberService.findMemberId(username);
            if (memberId == null) {
                throw new NullPointerException();
            }
            ModifyPointResDto modifyPointResDto = memberService.modifyPoint(memberId,
                    modifyPointDto.getPoint());
            return ResponseEntity.ok().body(modifyPointResDto);

        } catch (NotFoundException e) {
            log.error(PaypointStateCode.NOTEXIST.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.NOTEXIST));
        } catch (NullPointerException e) {
            log.error(PaypointStateCode.UNAVAILABLE.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(PaypointStateCode.UNAVAILABLE));
        } catch (RuntimeException e) {
            log.error(PaypointStateCode.UNKNOWN.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.UNKNOWN));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginReqDto loginReqDto){
        log.info("Member login - Call");
        try{
            LoginResDto loginResDto = memberService.login(loginReqDto);
            return ResponseEntity.ok().body(loginResDto);
        }catch (NotFoundException e){
            try{
                LoginResDto loginResDto =memberService.register(loginReqDto);
                return ResponseEntity.ok().body(loginResDto);
            }catch (Exception ex){
                return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.UNKNOWN));
            }
        } catch(Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.UNKNOWN));
        }
    }

    @PostMapping("/secure")
    public ResponseEntity<Object> secure(@RequestBody SecureReqDto secureReqDto){
        log.info("Member secure - Call");
        try{
            memberService.secure(secureReqDto.getPlayerId());
            return ResponseEntity.ok().build();
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.NOTEXIST));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse(PaypointStateCode.UNKNOWN));
        }
    }
    @GetMapping(value = "check",params = "playerId")
    public ResponseEntity method6(@RequestParam("playerId") String playerId) {
        if (memberService.getPlayerId(playerId)==null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().build();
        }
    }
}
