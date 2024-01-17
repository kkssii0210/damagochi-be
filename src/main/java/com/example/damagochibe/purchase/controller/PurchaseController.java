package com.example.damagochibe.purchase.controller;

import com.example.damagochibe.auth.security.CustomUserDetailService;
import com.example.damagochibe.auth.security.TokenProvider;
import com.example.damagochibe.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;


    @GetMapping("/accessToken")
    public void findMemberByAccessToken(@RequestHeader("Authorization") String accessToken) {
//        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
//            accessToken = accessToken.substring(7);
//        }
//        if (tokenProvider.validateToken(accessToken, customUserDetailService.loadUserByUsername(tokenProvider.getUsername(accessToken)))) {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String playerId = authentication.getName();
//            System.out.println("playerId = " + playerId);
//
//        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    }
}

