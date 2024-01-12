package com.example.damagochibe.management.training.service;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.management.mong.service.MongService1;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final MongRepository mongRepository;
    private final MongService1 mongService1;
    private final CooldownRepository cooldownRepository;

    public ResponseEntity training(Mong mong) {
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        if (!cooldownRepository.findByMongId(myMong.getId()).isTraining()) {
            if (myMong.getTired() != 0) {
                myMong.setTired(Math.max(myMong.getTired() - 20, 0));
                myMong.setExp(myMong.getExp() + 10);
                mongRepository.save(myMong);

                if (myMong.getExp() == 100 && myMong.getLevel() <= 9) {
                    mongService1.levelUp(myMong.getMemberId());
                    return ResponseEntity.ok("훈련하기 레벨업!!");
                }

//                return ResponseEntity.ok().build();
                return ResponseEntity.ok("훈련하기 경험치 + 10");
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void trainingCool(Mong mong) {
        Mong myMong  = mongRepository.findByMemberId(mong.getMemberId());
        Long mongId = myMong.getId();
        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        cooldown.setTraining(true);
        cooldownRepository.save(cooldown);
        try {
            Thread.sleep(10000);
            cooldown.setTraining(false);
            cooldownRepository.save(cooldown);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("feed 쿨다운 완료");
    }
}
