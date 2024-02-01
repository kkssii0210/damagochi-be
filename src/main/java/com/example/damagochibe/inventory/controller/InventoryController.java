package com.example.damagochibe.inventory.controller;

import com.example.damagochibe.inventory.enetity.Inventory;
import com.example.damagochibe.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<Inventory> getItems(String memberId) {
        return inventoryService.getItems(memberId);

    }

}
