package com.example.damagochibe.management.stroke.service;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

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

    @Async
    public void strokeCool(Mong mong) {
        Mong myMong  = mongRepository.findByMemberId(mong.getMemberId());
        Long mongId = myMong.getId();
        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        cooldown.setStroke(true);
        System.out.println("before update stroking..");

        cooldownRepository.save(cooldown);

        System.out.println("stroking..");
        System.out.println("System.identityHashCode(cooldown) = " + System.identityHashCode(cooldown));
        try {
            Thread.sleep(10000);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
        }

        strokeEnd(mongId);
        System.out.println("stroke 쿨다운 완료");
    }

    public void strokeEnd(Long mongId) {
        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        cooldown.setStroke(false);
        cooldownRepository.save(cooldown);
    }
}
