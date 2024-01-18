package com.example.damagochibe.monginfo.controller;

import com.example.damagochibe.auth.dto.request.MemberAuthDto;
import com.example.damagochibe.auth.security.CustomUserDetailService;
import com.example.damagochibe.auth.security.TokenProvider;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.service.MemberService;
import com.example.damagochibe.monginfo.dto.MongBattleDto;
import com.example.damagochibe.monginfo.entity.Mong;
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
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Mong>> getAllMongs() {
        List<Mong> mongs = mongInfoService.getAllMongs();
        return ResponseEntity.ok(mongs);
    }

    @GetMapping("id")
    public ResponseEntity<Mong> getMongById(@RequestHeader("Authorization") String accessToken) {
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        if (tokenProvider.validateToken(accessToken, customUserDetailService.loadUserByUsername(tokenProvider.getUsername(accessToken)))) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            MemberAuthDto dto = new MemberAuthDto(authentication.getName(), authentication.getAuthorities().stream().toList().get(0).toString());
            String playerId = dto.getPlayerId();
            Member byMemberPlayerId = memberService.findByMemberPlayerId(playerId);
            Long memberId = byMemberPlayerId.getMemberId();
            Mong mong = mongInfoService.findMongByMemberId(memberId);
            if (mong == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(mong);
        } else {
            return ResponseEntity.notFound().build();
        }
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

    @PutMapping("/battleInfo/{id}")
    public ResponseEntity<MongBattleDto> updateMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
        MongBattleDto updateBattleInfo = mongInfoService.updateMongBattleInfo(id, mongBattleDto);
//        updateBattleInfo.setWin(mongBattleDto.getWin());
//        updateBattleInfo.setLose(mongBattleDto.getLose());
//        return ResponseEntity.ok(updateBattleInfo);
        if (updateBattleInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateBattleInfo);
    }
}