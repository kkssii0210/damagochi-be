package com.example.damagochibe.purchase.service;

import com.example.damagochibe.Item.mapBackground.background.dto.FindMymapResDto;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.repository.CartRepository;
import com.example.damagochibe.inventory.enetity.Inventory;
import com.example.damagochibe.inventory.repository.InventoryRepository;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.repository.MemberRepository;
import com.example.damagochibe.purchase.dto.FindMymapListDto;
import com.example.damagochibe.purchase.entity.Purchase;
import com.example.damagochibe.purchase.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final MymapRepository mymapRepository;
    private final InventoryRepository inventoryRepository;

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
        Optional<Inventory> inventoryInfo = inventoryRepository.findByMemberIdAndItemName(memberId, itemName);

        if (presentInfo.isPresent() && inventoryInfo.isPresent()) {
            log.info("Call@@@@@");
            Purchase info = presentInfo.get();
            Integer oldCount = info.getOwnedItemCount();
            int newCount = oldCount + itemCount;
            info.setOwnedItemCount(newCount);
            purchaseRepository.save(info);

            //인벤토리에도 구매한 아이템 저장
            Inventory invenInfo = inventoryInfo.get();
            Integer oldQuantity = invenInfo.getQuantity();
            int newQuantity = oldQuantity + itemCount;
            invenInfo.setQuantity(newQuantity);
            inventoryRepository.save(invenInfo);


        } else if (presentInfo.isEmpty() && inventoryInfo.isEmpty()) {
            log.info("call!!!!!!");
            Purchase items = Purchase.builder()
                    .memberId(memberId)
                    .category(category)
                    .itemName(itemName)
                    .itemCode(itemCode)
                    .ownedItemCount(itemCount)
                    .build();
            purchaseRepository.save(items);

            Inventory inventory = Inventory.builder()
                    .memberId(memberId)
                    .category(category)
                    .name(itemName)
                    .itemCode(itemCode)
                    .quantity(itemCount)
                    .image("image")
                    .build();
            inventoryRepository.save(inventory);
            System.out.println("inventory.getMemberId() = " + inventory.getMemberId());
        }
    }

    public FindMymapListDto findMymap(String playerId) {
        Long memberId = memberRepository.findMemberIdByPlayerId(playerId);
        List<String> itemCodeByMemberId = purchaseRepository.findItemCodeByMemberId(memberId);
        List<String> fileUrls = itemCodeByMemberId.stream()
                .map(mymapRepository::findUrlByMapCode)
                .toList();

        return new FindMymapListDto(fileUrls);
    }
}
