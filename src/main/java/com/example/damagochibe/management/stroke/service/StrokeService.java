package com.example.damagochibe.management.stroke.service;

import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrokeService {

    private final MongRepository mongRepository;
    private boolean strokeCooldown = false;

    public ResponseEntity stroke(Mong mong) {
        System.out.println("strokeCooldown = " + strokeCooldown);
        if (!strokeCooldown) {
            Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
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

    public void strokeCool() {
        strokeCooldown = true;
        try {
            Thread.sleep(10000);
            strokeCooldown = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("stroke 쿨다운 완료");
    }
}
