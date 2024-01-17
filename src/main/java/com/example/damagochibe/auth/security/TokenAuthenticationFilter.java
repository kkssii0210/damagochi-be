package com.example.damagochibe.auth.security;

import com.example.damagochibe.auth.oauth.service.OauthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.LinkedHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("dofilter-call");
        String accessToken = getToken((HttpServletRequest) request);
        log.info("accessToken :" + accessToken);
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if (accessToken != null) {
            if (!requestURI.equals("/auth/logout") && !requestURI.equals("auth/isSocialMember")){
            //소셜회원인지 먼저 검증
            if (!tokenProvider.isSocialMember(accessToken)) {
                try {
                    // 토큰의 정보를 이용해 사용자 정보 생성
                    String username = tokenProvider.getUsername(accessToken);
                    UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                    System.out.println("username :" + username);

                    // 사용자 정보와 토큰을 이용해 토큰 검증
                    if (tokenProvider.validateToken(accessToken, userDetails)) {
                        // 이상이 없으면 인가 객체 생성해서 시큐리티 컨텍스트 공간에 저장
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        log.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication()));
                    }
                } catch (IllegalArgumentException | ExpiredJwtException e) {
                    throw new JwtException(e.getMessage());
                }
            }else {
                String email = getEmailFromKakaoAccessToken(accessToken);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }else{// 리프레쉬 토큰인 경우
                logger.info("JWT token 리프레쉬 or 로그아웃");
            }
        }
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
    private String getEmailFromKakaoAccessToken(String accessToken) {
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
}
