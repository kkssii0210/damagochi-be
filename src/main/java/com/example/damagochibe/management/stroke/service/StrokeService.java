package com.example.damagochibe.management.stroke.service;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrokeService {

    private final MongRepository mongRepository;
    private final CooldownRepository cooldownRepository;

    public ResponseEntity stroke(Mong mong) {
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        if (!cooldownRepository.findByMongId(myMong.getId()).isStroke()) {
            if (myMong.getTired() != 100) {
                myMong.setTired(Math.min(myMong.getTired() + 10, 100));
                mongRepository.save(myMong);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void strokeCool(Mong mong) {
        Mong myMong  = mongRepository.findByMemberId(mong.getMemberId());
        Long mongId = myMong.getId();
        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        cooldown.setStroke(true);
        cooldownRepository.save(cooldown);
        try {
            Thread.sleep(10000);
            cooldown.setStroke(false);
            cooldownRepository.save(cooldown);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("feed 쿨다운 완료");
    }
}
