package com.example.damagochibe.management.mong.service;

import com.example.damagochibe.management.mong.repository.MongRepository;

import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongService1 {

    private final MongRepository mongRepository;

    public Mong getInfo(String memberId) {
        Mong mymong = mongRepository.findByMemberId(memberId);
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

    public ResponseEntity evo(Mong mong) {
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        int level = myMong.getLevel();
        int evo = myMong.getEvolutionLevel();
        if (level >= 1 && evo == 1) {
            myMong.setEvolutionLevel(2);
            mongRepository.save(myMong);
            return ResponseEntity.ok().build();
        } else if (level >= 4 && evo == 2) {
            myMong.setEvolutionLevel(3);
            mongRepository.save(myMong);
            return ResponseEntity.ok().build();
        } else if (level >= 8 && evo == 3) {
            myMong.setEvolutionLevel(4);
            mongRepository.save(myMong);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public Map<String, Object> getUser(String userName, Long mongAId, Long mongBId) {
        Map<String, Object> map = new HashMap<>();
        Optional<Mong> userA = mongRepository.findById(mongAId);
        Optional<Mong> userB = mongRepository.findById(mongBId);
        map.put("userA", userA);
        map.put("userB", userB);
        map.put("userName", userName);
        return map;
    }
}
