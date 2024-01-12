package com.example.damagochibe.monginfo.controller;

import com.example.damagochibe.monginfo.entity.Mong;
import com.example.damagochibe.monginfo.service.MongInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monginfo")
public class MongInfoController {
    private final MongInfoService mongInfoService;

    @GetMapping
    public ResponseEntity<List<Mong>>getAllMongs(){
        List<Mong> mongs = mongInfoService.getAllMongs();
        return ResponseEntity.ok(mongs);
    }
    //Mong 조회
    @GetMapping("{id}")
    public ResponseEntity<Mong> getMongById(@PathVariable Long id){
        Mong mong = mongInfoService.getMongById(id);
        //mong 존재 여부
        if (mong == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mong);
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
// BATTLE COMTROLLER CRUD
//    @GetMapping("/battleInfo/{id}")
//    public ResponseEntity<MongBattleDto> getMongBattleInfo(@PathVariable Long id){
//        MongBattleDto battleInfo = mongInfoService.getMongBattleInfo(id);
//        if(battleInfo == null){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(battleInfo);
//    }
//    @PostMapping("/battle/{id}")
//    public ResponseEntity<MongBattleDto> createMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
//        MongBattleDto createdBattleInfo = mongInfoService.createMongBattleInfo(id, mongBattleDto);
//        return ResponseEntity.ok(createdBattleInfo);
//    }
//// Mong의 전투 정보 수정
//    @PutMapping("/battle/{id}")
//    public ResponseEntity<MongBattleDto> updateMongBattleInfo(@PathVariable Long id, @RequestBody MongBattleDto mongBattleDto) {
//        MongBattleDto updatedBattleInfo = mongInfoService.updateMongBattleInfo(id, mongBattleDto);
//        return ResponseEntity.ok(updatedBattleInfo);
//    }
}
