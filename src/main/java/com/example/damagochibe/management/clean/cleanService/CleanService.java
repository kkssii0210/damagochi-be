package com.example.damagochibe.management.clean.cleanService;

import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CleanService {
    private final MongRepository mongRepository;

    public ResponseEntity clean(Mong mong) {
        Mong myMong = mongRepository.findByMemberId(mong.getMemberId());
        if (!myMong.isClean()) {
            myMong.setClean(true);
            mongRepository.save(myMong);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
