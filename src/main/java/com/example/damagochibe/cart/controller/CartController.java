package com.example.damagochibe.cart.controller;

import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.service.CartService;
import com.example.damagochibe.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final AuthConfig authConfig;

    @GetMapping("/accessToken")
    public Cart findMemberAndCreateCart(HttpServletRequest request) {
        Member loginMember = authConfig.tokenValidationService(request);
        // 로그인한 유저의 playerId를 가져옴
        String playerId = loginMember.getPlayerId();

        if (loginMember != null) {
            // 로그인 한 멤버면 playerId로 Cart 정보 가져오기
            return cartService.findCartById(playerId);
        }
        return null;
    }

//    // 카트에 아이템 추가하기
//    @PostMapping("add")
//    public Cart addCart(Cart cart) {
//        // playerId로 가져와야함.
//        cartService.addCart();
//
//
//    }


}