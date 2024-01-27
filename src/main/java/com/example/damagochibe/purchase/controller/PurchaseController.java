package com.example.damagochibe.purchase.controller;

import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.auth.security.CustomUserDetailService;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.purchase.dto.PurchaseDto;
import com.example.damagochibe.purchase.dto.PurchasedListDto;
import com.example.damagochibe.purchase.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final CustomUserDetailService customUserDetailService;
    private final AuthConfig authConfig;

    @GetMapping("/accessToken")
    public ResponseEntity memberInfo(HttpServletRequest request) {
        Member loginMember = authConfig.tokenValidationService(request);

        if (loginMember != null) {

            return ResponseEntity.ok().body(loginMember);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/cartInfo")
    public ResponseEntity<Object> cartInfo(@RequestParam String playerId) {
        System.out.println("cart 정보를 위한 playerId = " + playerId);

        List<Cart> cartItem = purchaseService.getCartItem(playerId);
        return ResponseEntity.ok().body(cartItem);
    }

    @PostMapping("/buyItem")
    public void buyItem(@RequestBody PurchaseDto purchaseDto) {
        System.out.println("purchaseDto.getPlayerId() = " + purchaseDto.getPlayerId());
        System.out.println("purchaseDto.getRemainingPoint() = " + purchaseDto.getRemainingPoint());

        List<PurchasedListDto> purchasedItems = purchaseDto.getPurchasedItems();
        for (PurchasedListDto item : purchasedItems) {
            System.out.println("item.getItemCategory() = " + item.getItemCategory());
            System.out.println("item.getItemCount() = " + item.getItemCount());
            System.out.println("item.getItemName() = " + item.getItemName());

            String category = item.getItemCategory();
            String itemName = item.getItemName();
            Integer itemCount = item.getItemCount();
            String playerId = purchaseDto.getPlayerId();
            Integer remainingPoint = purchaseDto.getRemainingPoint();

            // 멤버 포인트 변경
            purchaseService.changeMemberPoint(playerId, remainingPoint);
            // 각 아이템 DB에 아이템을 소유한 member의 Id, 소유한 item quantity 저장
            purchaseService.savePurchaseInfoInItem(playerId, category, itemName, itemCount);
        }

    }


}




