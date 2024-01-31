package com.example.damagochibe.purchase.controller;

import com.example.damagochibe.Item.mapBackground.background.dto.FindMymapResDto;
import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.auth.security.CustomUserDetailService;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.code.MymapStateCode;
import com.example.damagochibe.exception.NotFoundMymapException;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.purchase.dto.FindMymapListDto;
import com.example.damagochibe.purchase.dto.PurchaseDto;
import com.example.damagochibe.purchase.dto.PurchasedListDto;
import com.example.damagochibe.purchase.service.PurchaseService;
import com.example.damagochibe.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
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
    @GetMapping("/myMapList")
    public ResponseEntity<Object> myMapList(@RequestHeader(value = "MemberId") String playerId) {
        log.info("myMapList - Call");
        try {
            FindMymapListDto ret = purchaseService.findMymap(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(ret);
        }catch (NotFoundMymapException e){
            log.info("code : {}, message : {}", MymapStateCode.MYMAP_ERROR.getCode(), MymapStateCode.MYMAP_ERROR.name());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(MymapStateCode.MYMAP_ERROR));
        }
        catch (Exception e){
            System.out.println(e);
            log.info("code : {}, message : {}", MymapStateCode.UNKNOWN.getCode(), MymapStateCode.UNKNOWN.name());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(MymapStateCode.UNKNOWN));
        }
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
            System.out.println("item.itemCode() = " + item.getItemCode());

            String category = item.getItemCategory();
            Integer itemCount = item.getItemCount();
            String itemName = item.getItemName();
            String itemCode = item.getItemCode();
            String playerId = purchaseDto.getPlayerId();
            Integer remainingPoint = purchaseDto.getRemainingPoint();

            // 멤버 포인트 변경
            purchaseService.changeMemberPoint(playerId, remainingPoint);
            // 구매아이템 DB에 아이템을 소유한 member의 Id, 소유한 item quantity 저장
            purchaseService.savePurchaseInfoInItem(playerId, category, itemName, itemCount, itemCode);
        }

    }


}




