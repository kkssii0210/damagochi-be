package com.example.damagochibe.purchase.service;

import com.example.damagochibe.Item.food.entity.Food;
import com.example.damagochibe.Item.food.repository.FoodRepository;
import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import com.example.damagochibe.Item.liquidMedicine.repository.LiquidMedicineRepository;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.repository.CartRepository;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.repository.MemberRepository;
import com.example.damagochibe.purchase.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;
    private final LiquidMedicineRepository liquidMedicineRepository;
    private final MymapRepository mymapRepository;

    // 멤버 정보 불러오기
    public void purchase(Long memberId) {
        memberRepository.findByMemberId(memberId);
    }

    // 카트 정보 불러오기
    public List<Cart> getCartItem(String playerId) {
        return cartRepository.findCartByPlayerId(playerId);
    }

    // 남은 포인트 정보 교체
    public void changeMemberPoint(String playerId, Integer remainingPoint) {
        Optional<Member> member = memberRepository.findByPlayerId(playerId);
        if (member.isPresent()) {
            Member memberInfo = member.get();
            memberInfo.setPoint(remainingPoint);
            memberRepository.save(memberInfo);
            System.out.println("서비스에서 memberInfo.getPoint() = " + memberInfo.getPoint());
        }
    }

    public void savePurchaseInfoInItem(String playerId, String category, String itemName, Integer itemCount) {
        System.out.println("서비스에 넘어가는지 확인 playerId = " + playerId);
        System.out.println("category = " + category);
        System.out.println("itemName = " + itemName);
        System.out.println("itemCount = " + itemCount);

        Long memberId = memberRepository.findMemberIdByPlayerId(playerId);

        if (category.equals("food")) {
            // 구매한 아이템이 여러개인 경우, 여러개 저장되는지 봐야함
            Food food = foodRepository.findByName(itemName);
            food.setMemberId(memberId);
            food.setMemberOwnedQuantity(itemCount);

            foodRepository.save(food);
        }

        if (category.equals("liquidMedicine")) {
            LiquidMedicine liquidMedicine = liquidMedicineRepository.findByName(itemName);
            liquidMedicine.setMemberId(memberId);
            liquidMedicine.setMemberOwnedQuantity(itemCount);

            liquidMedicineRepository.save(liquidMedicine);
        }

        if (category.equals("map")) {
            Mymap map = mymapRepository.findByName(itemName);
            map.setMemberId(memberId);
            map.setMemberOwnedQuantity(itemCount);

            mymapRepository.save(map);
        }

    }
}
