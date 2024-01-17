package com.example.damagochibe.management.training.service;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.management.mong.service.MongService1;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final MongRepository mongRepository;
    private final MongService1 mongService1;
    private final CooldownRepository cooldownRepository;

    public ResponseEntity training(Mong mong) {
        System.out.println("train1");
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        System.out.println("train2");
        if (!cooldownRepository.findByMongId(myMong.getId()).isTraining()) {
            System.out.println("train3");
            if (myMong.getTired() != 0) {
                System.out.println("train4");
                myMong.setTired(Math.max(myMong.getTired() - 20, 0));
                System.out.println("train5");
                myMong.setExp(myMong.getExp() + 10);
                System.out.println("train6");
                mongRepository.save(myMong);
                System.out.println("train7");

                if (myMong.getExp() == 100 && myMong.getLevel() <= 9) {
                    System.out.println("train8");
                    mongService1.levelUp(myMong.getMemberId());
                    System.out.println("train9");
                    return ResponseEntity.ok("훈련하기 레벨업!!");
                }
                 System.out.println("train10");

//                return ResponseEntity.ok().build();
                return ResponseEntity.ok("훈련하기 경험치 + 10");
            } else {
                System.out.println("train11");
                return ResponseEntity.badRequest().build();
            }
        } else {
            System.out.println("train12");
            return ResponseEntity.notFound().build();
        }
    }

    @Async
    public void trainingCool(Mong mong) {
        System.out.println("before update training..1");

        Mong myMong  = mongRepository.findByMemberId(mong.getMemberId());
        System.out.println("before update training..2");

        Long mongId = myMong.getId();
        System.out.println("before update training..3");

        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        System.out.println("before update training..4");

        cooldown.setTraining(true);
        System.out.println("before update training..5");

        cooldownRepository.save(cooldown);

        System.out.println("training..");
        System.out.println("System.identityHashCode(cooldown) = " + System.identityHashCode(cooldown));
        try {
            Thread.sleep(10000);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        trainingEnd(mongId);
        System.out.println("train 쿨다운 완료");
    }

    public void trainingEnd(Long mongId) {
        Cooldown cooldown = cooldownRepository.findByMongId(mongId);
        cooldown.setTraining(false);
        cooldownRepository.save(cooldown);
    }
}
