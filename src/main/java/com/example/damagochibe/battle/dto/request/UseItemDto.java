package com.example.damagochibe.battle.dto.request;

import com.example.damagochibe.battle.dto.response.BattleMessageResDto;
import com.example.damagochibe.inventory.enetity.Inventory;
import lombok.Data;

@Data
public class UseItemDto {
    private BattleMessageResDto battleMessageResDto;
    private Inventory inventory;
}
