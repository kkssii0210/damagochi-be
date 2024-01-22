package com.example.damagochibe.cart.controller;

import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.cart.dto.CartReqDto;
import com.example.damagochibe.cart.dto.CartResDto;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.service.CartService;
import com.example.damagochibe.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final AuthConfig authConfig;

    @GetMapping("/accessToken")
    public ResponseEntity findMemberAndGetCart(HttpServletRequest request) {
        Member loginMember = authConfig.tokenValidationService(request);
        // localStorage의 액세스 토큰과
        // 서버에 저장된 액세스 토큰에 대응하는 멤버가 없으면 unAuthorized, 아니면 ok
        if (loginMember != null) {
            return ResponseEntity.ok().body(loginMember);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 카트에 아이템 추가하기
    @PostMapping("/add")
    public ResponseEntity addCart(@RequestBody CartReqDto cartReqDto) {
        System.out.println("cartReqDto.getStoreId() = " + cartReqDto.getStoreId());
        System.out.println("cartReqDto.getCategory() = " + cartReqDto.getCategory());
        System.out.println("cartReqDto.getPlayerId() = " + cartReqDto.getPlayerId());
        System.out.println("cartReqDto.getItemCount() = " + cartReqDto.getItemCount());
        System.out.println("cartReqDto.getItemName() = " + cartReqDto.getItemName());
        cartService.addCart(cartReqDto);

        return ResponseEntity.noContent().build();
    }

    // 카트 정보 가져오기
    @GetMapping("/itemInfo")
    public ResponseEntity<Object> cartItem(@RequestParam String playerId) {
        System.out.println("카트 정보 가져오기 playerId = " + playerId);

        List<Cart> cartItem = cartService.getCartItem(playerId);
        System.out.println("cartItem.get(0).getPlayerId() = " + cartItem.get(0).getPlayerId());
        return ResponseEntity.ok().body(cartItem);
    }

    @DeleteMapping("/delete")
    public void cartItemDelete(@RequestBody CartReqDto cartReqDto) {
        System.out.println("cartReqDto.getPlayerId() = " + cartReqDto.getPlayerId());
        System.out.println("cartReqDto.getItemName() = " + cartReqDto.getItemName());
        cartService.deleteCartItem(cartReqDto);
    }


}