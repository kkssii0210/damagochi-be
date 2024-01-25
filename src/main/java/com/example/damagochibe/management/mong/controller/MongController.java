package com.example.damagochibe.management.mong.controller;

import com.example.damagochibe.auth.security.CustomUserDetail;
import com.example.damagochibe.battle.dto.response.BattleMessageResDto;
import com.example.damagochibe.battle.service.BattleService;
import com.example.damagochibe.management.mong.service.MongService1;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/mong")
public class MongController {

    private final MongService1 mongService1;
    private final BattleService battleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public Mong getInfo(@AuthenticationPrincipal CustomUserDetail principal) {
        System.out.println("principal.getUsername() = " + principal.getUsername());
        return mongService1.getInfo(principal.getUsername());
    }

    @PutMapping("/levelUp")
    public ResponseEntity levelUp(@RequestBody Mong mong) {
        return mongService1.levelUp(mong.getMemberId());
    }

    @PutMapping("/evo")
    public ResponseEntity evo(@RequestBody Mong mong) {
        System.out.println("mong = " + mong);
        return mongService1.evo(mong);
    }

    @GetMapping("/getUser")
    public Map<String, Object> getUser(@AuthenticationPrincipal CustomUserDetail principal,
                                       @RequestParam("userAMongId") Long userAMongId,
                                       @RequestParam("userBMongId") Long userBMongId) {
        return mongService1.getUser(principal.getUsername(), userAMongId, userBMongId);
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PutMapping
    public BattleMessageResDto attack(@RequestBody BattleMessageResDto resDto) {
        System.out.println("resDto = " + resDto);
        BattleMessageResDto rrr = battleService.attack(resDto);
        System.out.println("rrr = " + rrr);
        simpMessagingTemplate.convertAndSend("/topic/battleRooms/page/"+ resDto.getBattleRoomId(), rrr);

        return rrr;
    }
}
