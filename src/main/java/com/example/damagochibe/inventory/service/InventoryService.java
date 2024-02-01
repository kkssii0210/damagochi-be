package com.example.damagochibe.inventory.service;

import com.example.damagochibe.Item.liquidMedicine.repository.LiquidMedicineRepository;
import com.example.damagochibe.inventory.enetity.Inventory;
import com.example.damagochibe.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final LiquidMedicineRepository liquidMedicineRepository;

    public List<Inventory> getItems(Long memberId) {
        List<Inventory> itemList = inventoryRepository.findAllByMemberId(memberId);

        for (Inventory item : itemList) {
            item.setImage(liquidMedicineRepository.findByCode(item.getItemCode()));
        }

        return itemList;

    }
}
