package com.example.damagochibe.monginfo.controller;

import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.auth.dto.request.MemberAuthDto;
import com.example.damagochibe.auth.security.CustomUserDetailService;
import com.example.damagochibe.auth.security.TokenProvider;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.service.MemberService;
import com.example.damagochibe.monginfo.dto.MongBattleDto;
import com.example.damagochibe.monginfo.entity.Mong;
import com.example.damagochibe.monginfo.repository.MongInfoRepo;
import com.example.damagochibe.monginfo.service.MongInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monginfo")
public class MongInfoController {
    private final MongInfoService mongInfoService;
    private final AuthConfig authConfig;
    private final MongInfoRepo mongInfoRepo;

    @GetMapping
    public ResponseEntity<List<Mong>> getAllMongs() {
        List<Mong> mongs = mongInfoService.getAllMongs();
        return ResponseEntity.ok(mongs);
    }

    @GetMapping("/id")
    public ResponseEntity getMongById(@RequestHeader("Authorization") String accessToken) {
        System.out.println("!!!! = "+accessToken);
        accessToken = accessToken.substring(7);
        Member member = authConfig.tokenValidationServiceV1(accessToken);
        System.out.println("44444 = " +member.getMemberId());
        Mong mongByMemberId = mongInfoRepo.findMongByMemberId(member.getPlayerId());
        System.out.println("5555 = "+mongByMemberId.getId());
        if (mongByMemberId != null) {
            return ResponseEntity.ok(mongByMemberId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Mong> addMong(@RequestBody Mong mong) {
        Mong addMong = mongInfoService.addMong(mong);
        return ResponseEntity.status(HttpStatus.CREATED).body(addMong);
    }

    @PutMapping("{id}")
    public ResponseEntity<Mong> updateMong(@PathVariable Long id, @RequestBody Mong mong) {
        Mong updatedMong = mongInfoService.updateMong(id, mong);
        if (updatedMong == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMong(@PathVariable Long id) {
        mongInfoService.deleteMong(id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/battleInfo/{id}")
    public ResponseEntity<MongBattleDto> getMongBattleInfo(@PathVariable Long id) {
        MongBattleDto battleInfo = mongInfoService.getMongBattleInfo(id);
        if (battleInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(battleInfo);
    }

//    @PutMapping("/battleInfo/{id}")
//    public ResponseEntity<MongBattleDto> updateMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
//        MongBattleDto existingInfo = mongInfoService.updateMongBattleInfo(id);
//        if (mongBattleDto.getWin() > 0) {
//            existingInfo.setWin(existingInfo.getWin() + 1);
//        } else {
//            existingInfo.setLose(existingInfo.getLose() + 1);
//        }
//        return ResponseEntity.ok(existingInfo);
//    }
    @PutMapping("/battleInfo/{id}/win")
    public ResponseEntity<MongBattleDto> updateWin(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto){
        MongBattleDto existingInfo = mongInfoService.updateWin(id);
        existingInfo.setWin(existingInfo.getWin() + 1);
        return ResponseEntity.ok(existingInfo);
    }

    @PutMapping("/battleInfo/{id}/lose")
    public ResponseEntity<MongBattleDto> updateLose(@PathVariable Long id, @PathVariable MongBattleDto mongBattleDto){
        MongBattleDto existingInfo = mongInfoService.updateLose(id);
        existingInfo.setLose(existingInfo.getLose() + 1);
        return ResponseEntity.ok(existingInfo);

    }
}
