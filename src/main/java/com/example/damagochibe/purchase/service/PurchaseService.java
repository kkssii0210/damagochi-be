package com.example.damagochibe.purchase.service;

import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.repository.CartRepository;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.repository.MemberRepository;
import com.example.damagochibe.purchase.entity.Purchase;
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

    // 구매 내역 저장
    public void savePurchaseInfoInItem(String playerId, String category, String itemName, Integer itemCount, String itemCode) {
        System.out.println("서비스에 넘어가는지 확인 playerId = " + playerId);
        System.out.println("category = " + category);
        System.out.println("itemName = " + itemName);
        System.out.println("itemCount = " + itemCount);
        System.out.println("itemCode = " + itemCode);

        // playerId로 memberId찾아서 purchase 리포지토리에 저장
        Long memberId = memberRepository.findMemberIdByPlayerId(playerId);

        // 만약 itemName, memberId로 찾았는데 이미 존재하면 item수량만 기존 수량에서 +
        Optional<Purchase> presentInfo = purchaseRepository.findByMemberIdAndItemName(memberId, itemName);

        if (presentInfo.isPresent()) {
            Purchase info = presentInfo.get();
            Integer oldCount = info.getOwnedItemCount();
            int newCount = oldCount + itemCount;
            info.setOwnedItemCount(newCount);
            purchaseRepository.save(info);

        } else if (presentInfo.isEmpty()) {
            Purchase items = Purchase.builder()
                    .memberId(memberId)
                    .category(category)
                    .itemName(itemName)
                    .itemCode(itemCode)
                    .ownedItemCount(itemCount)
                    .build();
            purchaseRepository.save(items);
        }
    }

}
