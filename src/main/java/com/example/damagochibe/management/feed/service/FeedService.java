package com.example.damagochibe.management.feed.service;

import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final MongRepository mongRepository;

    private boolean feedCooldown = false;

    public ResponseEntity feed(Mong mong) {
        if (!feedCooldown) {
            Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
            if (myMong.getFeed() != 100) {
                myMong.setFeed(Math.min(myMong.getFeed() + 10, 100));
                mongRepository.save(myMong);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void feedCool() {
        feedCooldown = true;
        try {
            Thread.sleep(10000);
            feedCooldown = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("feed 쿨다운 완료");
    }
}
