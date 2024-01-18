package com.example.damagochibe.inventory.service;

import com.example.damagochibe.inventory.enetity.Inventory;
import com.example.damagochibe.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<Inventory> getItems(String memberId) {
        return inventoryRepository.findAllByMemberId(memberId);

    }
}
