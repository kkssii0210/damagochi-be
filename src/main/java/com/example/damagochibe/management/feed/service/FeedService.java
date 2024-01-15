package com.example.damagochibe.management.feed.service;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final MongRepository mongRepository;
    private final CooldownRepository cooldownRepository;

    public ResponseEntity feed(Mong mong) {
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        if (!cooldownRepository.findByMongId(myMong.getId()).isFeed()) {
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

    public void feedCool(Mong mong) {
        Mong myMong  = mongRepository.findByMemberId(mong.getMemberId());
        Long mongId = myMong.getId();
        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        cooldown.setFeed(true);
        System.out.println("before update feeding..");
        cooldownRepository.save(cooldown);

        System.out.println("feeding..");
        System.out.println("System.identityHashCode(cooldown) = " + System.identityHashCode(cooldown));
        try {
            Thread.sleep(10000);
            cooldown.setFeed(false);
            cooldownRepository.save(cooldown);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("feed 쿨다운 완료");
    }

    public void feedDirty(Mong mong) {
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        myMong.setClean(false);
        mongRepository.save(myMong);
    }
}
