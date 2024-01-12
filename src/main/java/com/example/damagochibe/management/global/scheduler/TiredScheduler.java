package com.example.damagochibe.management.global.scheduler;

import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class TiredScheduler {

    private final MongRepository mongRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled( cron = "0 32 * * * *")   // 매 시 0분 0초 메소드 실행
    public void tiredSchedule() {
        List<Mong> mongList = mongRepository.findAll();
//        TODO : 나중에 리스트로 교체, 시 분 제대로 맞추기

        for (Mong mong : mongList ) {
            mong.setTired(Math.max(0, mong.getTired() - 5));
            mong.setFeed(Math.max(0, mong.getFeed() - 20));
            System.out.println("mong.getTired() = " + mong.getTired());
            mongRepository.save(mong);
        }

        simpMessagingTemplate.convertAndSend("/topic/management", "ㄹㅇ");
    }

}
