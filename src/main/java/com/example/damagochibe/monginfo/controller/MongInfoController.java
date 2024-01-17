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
    public ResponseEntity<List<Mong>>getAllMongs(){
        List<Mong> mongs = mongInfoService.getAllMongs();
        return ResponseEntity.ok(mongs);
    }
    //Mong 조회
    //TODO: 추후 리팩토링 필요함.
    @GetMapping("id")
    public ResponseEntity<Mong> getMongById(@RequestHeader("Authorization")String accessToken){
        if(StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")){
            accessToken = accessToken.substring(7);
        }
        if(tokenProvider.validateToken(accessToken, customUserDetailService.loadUserByUsername(tokenProvider.getUsername(accessToken)))){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            MemberAuthDto dto = new MemberAuthDto(authentication.getName(),authentication.getAuthorities().stream().toList().get(0).toString());
            String playerId = dto.getPlayerId();
            Member byMemberPlayerId = memberService.findByMemberPlayerId(playerId);
            Long memberId = byMemberPlayerId.getMemberId();
            Mong mong = mongInfoService.findMongByMemberId(memberId);
            if (mong == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(mong);
        }else {
            return ResponseEntity.notFound().build();
        }
//멤버 아이디로 mong가져오는 로직필요,
        //mong 존재 여부
    }
    //새로운 Mong 생성 create
    @PostMapping
    public ResponseEntity<Mong> addMong(@RequestBody Mong mong){
        Mong addMong = mongInfoService.addMong(mong);
        return ResponseEntity.status(HttpStatus.CREATED).body(addMong);
    }
//    Mong Info 수정......?????? TODO : 다시 확인 할 것 : 내가 구현하려는 것, Mong에 대한 정보를 보여주는 것..... CRUD 혼동...
    //아래_수정 메소드 _ 이전 프로젝트랑 비교해보기
    @PutMapping("{id}")
    public ResponseEntity<Mong> updateMong(@PathVariable Long id, @RequestBody Mong mong ){
        Mong updatedMong = mongInfoService.updateMong(id, mong);
        if(updatedMong == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMong);
    }
    //삭제 메소드 delete method
    @DeleteMapping("/{id}")
//    public ResponseEntity<???>
public ResponseEntity<Void> deleteMong(@PathVariable Long id){
        mongInfoService.deleteMong(id);
        return ResponseEntity.notFound().build();
    }
// BATTLE COTROLLEnR CRUD
    @GetMapping("/battleInfo/{id}")
    public ResponseEntity<MongBattleDto> getMongBattleInfo(@PathVariable Long id){
        MongBattleDto battleInfo = mongInfoService.getMongBattleInfo(id);
        if(battleInfo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(battleInfo);
    }
    @PostMapping("/battleInfo/{id}")
    public ResponseEntity<MongBattleDto> createMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
        MongBattleDto createdBattleInfo = mongInfoService.createMongBattleInfo(id, mongBattleDto);
        return ResponseEntity.ok(createdBattleInfo);
    }
    @PutMapping("/battleInfo/{id}")
    public ResponseEntity<MongBattleDto> updateMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
        MongBattleDto updatedBattleInfo = mongInfoService.updateMongBattleInfo(id, mongBattleDto);

        if (mongBattleDto.getMongId() > mongBattleDto.getOpponentMongId()) { // 승리한 경우
            updatedBattleInfo.setWin(updatedBattleInfo.getWin() + 1);
        } else { // 패배한 경우
            updatedBattleInfo.setLose(updatedBattleInfo.getLose() + 1);
        }

        return ResponseEntity.ok(updatedBattleInfo);
    }

    @PutMapping("/battleInfo/{id}")
    public ResponseEntity<MongBattleDto> updateMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
        MongBattleDto updatedBattleInfo = mongInfoService.updateMongBattleInfo(id, mongBattleDto);

        if (mongBattleDto.getWin() > mongBattleDto.getLose()) {  // 승패 판단 수정
            updatedBattleInfo.setWin(updatedBattleInfo.getWin() + 1);
        } else {
            updatedBattleInfo.setLose(updatedBattleInfo.getLose() + 1);
        }

        return ResponseEntity.ok(updatedBattleInfo);
    }
}