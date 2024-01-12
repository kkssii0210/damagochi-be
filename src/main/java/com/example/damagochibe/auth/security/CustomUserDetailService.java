package com.example.damagochibe.auth.security;

import com.example.damagochibe.member.dto.request.SecureReqDto;
import com.example.damagochibe.member.dto.response.SecureResDto;
import com.example.damagochibe.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String playerId) throws UsernameNotFoundException {
        log.debug("loadUseByUsename");
        SecureReqDto secureReqDto = SecureReqDto.builder().playerId(playerId).build();
        ObjectMapper om = new ObjectMapper();
        SecureResDto secureResDto = null;
        try {
            secureResDto = om.convertValue(
                    memberService.secure(secureReqDto.getPlayerId()), SecureResDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        log.debug(secureResDto.toString());
        return CustomUserDetail.of(secureResDto);
    }
}
