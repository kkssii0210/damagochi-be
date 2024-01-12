package com.example.damagochibe.auth.config;

import com.example.damagochibe.auth.oauth.service.OauthService;
import com.example.damagochibe.auth.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final TokenExceptionFilter tokenExceptionFilter;
    private final TokenEntryPoint tokenEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // csrf disable
                .csrf(csrf->csrf.disable())
//                .authorizeRequests() // 요청에 대한 사용권한 체크
//                .antMatchers("/auth/login", "/auth/reissue", "/auth/login/watch").permitAll()
//                .antMatchers("/auth/**").hasAnyAuthority("USER")
//                .anyRequest().permitAll()
                .exceptionHandling(exceptionHandling->exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(tokenEntryPoint)
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/auth/login","/auth/isSocialMember").permitAll()
                        .requestMatchers("/auth/accessToken").authenticated()
                        .requestMatchers("/auth/**").hasAnyAuthority("USER")
                        .anyRequest().permitAll()
                )

                .sessionManagement(sessionManagement->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        http.addFilterBefore(new TokenAuthenticationFilter(tokenProvider, customUserDetailService),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenExceptionFilter, TokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
