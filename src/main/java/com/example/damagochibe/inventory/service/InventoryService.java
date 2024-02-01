package com.example.damagochibe.inventory.service;

import com.example.damagochibe.Item.liquidMedicine.repository.LiquidMedicineRepository;
import com.example.damagochibe.inventory.enetity.Inventory;
import com.example.damagochibe.inventory.repository.InventoryRepository;
import com.example.damagochibe.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final LiquidMedicineRepository liquidMedicineRepository;
    private final MemberRepository memberRepository;

    public List<Inventory> getItems(String memberId) {
        Long memberIdByPlayerId = memberRepository.findMemberIdByPlayerId(memberId);
        List<Inventory> itemList = inventoryRepository.findAllByMemberId(memberIdByPlayerId);
        for (Inventory item : itemList) {
            item.setImage(liquidMedicineRepository.findByCode(item.getItemCode()));
        }

        return itemList;

    }
}
