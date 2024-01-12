package com.example.damagochibe.management.sleep.service;

import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SleepService {

    private final MongRepository mongRepository;

//TODO: 임시 구현 나중에 쿨타임 적용, 수치 조정
    public ResponseEntity sleep(String memberId) {
        Mong mymong = mongRepository.findByMemberId(memberId);
        mymong.setTired(100);
        mongRepository.save(mymong);
        return ResponseEntity.ok().build();
    }

}
