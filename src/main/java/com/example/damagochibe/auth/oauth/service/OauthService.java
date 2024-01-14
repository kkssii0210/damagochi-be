package com.example.damagochibe.auth.oauth.service;

import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.auth.entity.Auth;
import com.example.damagochibe.auth.entity.RefreshToken;
import com.example.damagochibe.auth.oauth.dto.SocialOauthToken;
import com.example.damagochibe.auth.oauth.dto.SocialUserDto;
import com.example.damagochibe.auth.repository.AuthRepository;
import com.example.damagochibe.auth.repository.RefreshTokenRepository;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    private final MemberRepository memberRepository;
    private final MymapRepository mymapRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    public SocialUserDto getKakaoMember(String accessToken) {
        String email = getEmailFromKakaoAccessToken(accessToken);
        log.info("email: "+email);
        Optional<Member> result = memberRepository.findByPlayerId(email);
        //기존 회원이면
        if (result.isPresent()){
            return entityToDTO(result.get());
        }
        //기존 회원이 아니면 자동등록
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);
        Long memberId = socialMember.getMemberId();
        Mymap mymap = Mymap.builder()
                .mapCode("MP000")
                .memberId(memberId)
                .build();
        mymapRepository.save(mymap);
        authRepository.save(
                Auth.builder().memberId(String.valueOf(memberId)).role("USER")
                        .build());
        return entityToDTO(socialMember);
    }

    public Member makeSocialMember(String email) {
        String tempPassword = makeTempPassword();
        log.info("tempPassword :" + tempPassword);
        return Member.builder()
                .playerId(email)
                .password(passwordEncoder.encode(tempPassword))
                .point(0)
                .isSocialMember(true)
                .build();
    }

    public String getEmailFromKakaoAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
        if(accessToken == null){
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                uriBuilder.toString(),
                HttpMethod.GET,
                entity,
                LinkedHashMap.class);
        log.info(String.valueOf(response));
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("-----------------------");
        log.info(String.valueOf(bodyMap));
        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
        log.info("kakaoAccount: " + kakaoAccount);
        return kakaoAccount.get("email");
    }
    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int)(Math.random()*55)+65));
        }
        return buffer.toString();
    }
    private SocialUserDto entityToDTO(Member member){
        return new SocialUserDto(member.getPlayerId(), member.getPassword(), member.getPoint(), member.getIsSocialMember());
    }
    public Member generateToken(SocialOauthToken socialOauthToken) {
        String email = getEmailFromKakaoAccessToken(socialOauthToken.getAccess_token());
        Optional<Member> result = memberRepository.findByPlayerId(email);
        if (result.isPresent()){
            Member member = result.get();
            Long memberId = result.get().getMemberId();
            RefreshToken socialToken = RefreshToken.builder()
                    .id(email)
                    .accessToken(socialOauthToken.getAccess_token())
                    .expiration(new Date(System.currentTimeMillis()+ (long) socialOauthToken.getExpires_in() * 1000L))
                    .refreshToken(socialOauthToken.getRefresh_token())
                    .refreshTokenExpiresIn(new Date(System.currentTimeMillis()+ (long) socialOauthToken.getRefresh_token_expires_in() * 1000L))
                    .memberId(memberId)
                    .build();
            refreshTokenRepository.save(socialToken);
            return member;
        }
        return null;
    }
}
