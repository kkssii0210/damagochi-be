package com.example.damagochibe.Item.mapBackground.background.service;

import com.example.damagochibe.Item.mapBackground.background.dto.FindMymapResDto;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.exception.NotFoundMymapException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MymapService {
    private final MymapRepository mymapRepository;

    @Transactional
    public FindMymapResDto findMymap(String memberIdStr) throws RuntimeException{
        Long memberId = Long.parseLong(memberIdStr);
        LocalDateTime aHourAgo = LocalDateTime.now().minusHours(1L);
        System.out.println("한시간전:"+aHourAgo);
        Mymap mymap = mymapRepository.getByMemberId(memberId).orElseThrow(() -> new NotFoundMymapException());
        if( mymap.getUpdDt().isBefore(aHourAgo )){
            mymap.setMapCode("MP000");
            mymap.setUpdDt(LocalDateTime.now());
        }
        FindMymapResDto ret = new FindMymapResDto(mymap.getMapCode());
        return ret;
    }

    public void setMymap(Long memberId, String mapCode) throws RuntimeException {
        Mymap mymap = mymapRepository.getByMemberId(memberId).orElseThrow(() -> new NotFoundMymapException());
        mymap.setMapCode(mapCode);
        LocalDateTime now = LocalDateTime.now();
        mymap.setUpdDt(now);

    }
}
