package com.example.damagochibe.management.mong.service;

import com.example.damagochibe.management.mong.repository.MongRepository;

import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongService1 {

    private final MongRepository mongRepository;

    public Mong getInfo(String memberId) {
        Mong mymong = mongRepository.findByMemberId(memberId);
//        if (mymong.getTired() <= 50) {
//            mymong.set("졸림");
//        } else if (mong.feed <= 50) {
//            setCondition("배고픔")
//        } else if (mong.tired >= 80 && mong.feed >= 80) {
//            setCondition("신남")
//        } else {
//            setCondition("보통")
//        }

        return mymong;
    }


    public ResponseEntity levelUp(String memberId) {
        Mong myMong = mongRepository.findByMemberId(memberId);
        myMong.setExp(0);
        myMong.setLevel(myMong.getLevel() + 1);
        myMong.setStrength(myMong.getStrength() + ((int) (Math.random() * 2.0) + 2));
        myMong.setAgility(myMong.getAgility() + ((int) (Math.random() * 2.0) + 2));
        myMong.setDefense(myMong.getDefense() + ((int) (Math.random() * 2.0) + 2));
        mongRepository.save(myMong);
        return ResponseEntity.ok().build();
    }
}
