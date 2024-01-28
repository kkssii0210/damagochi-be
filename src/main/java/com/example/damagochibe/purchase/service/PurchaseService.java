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
            Food food = foodRepository.findByName(itemName);

            if (food.getMemberId() == null) {
                food.setMemberId(memberId);
                food.setMemberOwnedQuantity(itemCount);
                foodRepository.save(food);
            } else {
                Food newPurchaseInfo = Food.builder()
                        .category(food.getCategory())
                        .fileUrl(food.getFileUrl())
                        .foodCode(food.getFoodCode())
                        .foodFunction(food.getFoodFunction())
                        .foodName(food.getFoodName())
                        .foodPrice(food.getFoodPrice())
                        .memberId(memberId)
                        .memberOwnedQuantity(itemCount)
                        .build();

                foodRepository.save(newPurchaseInfo);
            }
        }

        if (category.equals("liquidMedicine")) {
            LiquidMedicine liquidMedicine = liquidMedicineRepository.findByName(itemName);

            if (liquidMedicine.getMemberId() == null) {
                liquidMedicine.setMemberId(memberId);
                liquidMedicine.setMemberOwnedQuantity(itemCount);
                liquidMedicineRepository.save(liquidMedicine);
            } else {
                LiquidMedicine newPurchaseInfo = LiquidMedicine.builder()
                        .category(liquidMedicine.getCategory())
                        .fileUrl(liquidMedicine.getFileUrl())
                        .liquidMedicineCode(liquidMedicine.getLiquidMedicineCode())
                        .liquidMedicineFunction(liquidMedicine.getLiquidMedicineFunction())
                        .liquidMedicineName(liquidMedicine.getLiquidMedicineName())
                        .liquidMedicinePrice(liquidMedicine.getLiquidMedicinePrice())
                        .memberId(memberId)
                        .memberOwnedQuantity(itemCount)
                        .build();
                liquidMedicineRepository.save(newPurchaseInfo);
            }
        }


        if (category.equals("map")) {
            Mymap map = mymapRepository.findByName(itemName);

            if (map.getMemberId() == null) {
                map.setMemberId(memberId);
                map.setMemberOwnedQuantity(itemCount);
                mymapRepository.save(map);
            } else {
                Mymap newPurchaseInfo = Mymap.builder()
                        .category(map.getCategory())
                        .fileUrl(map.getFileUrl())
                        .mapCode(map.getMapCode())
                        .mapFunction(map.getMapFunction())
                        .mapName(map.getMapName())
                        .mapPrice(map.getMapPrice())
                        .memberId(memberId)
                        .memberOwnedQuantity(itemCount)
                        .build();
                mymapRepository.save(newPurchaseInfo);
            }

        }

    }
}
