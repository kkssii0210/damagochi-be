package com.example.damagochibe.monginfo.service;

import com.example.damagochibe.monginfo.dto.MongBattleDto;
import com.example.damagochibe.monginfo.dto.MongInfoDto;
import com.example.damagochibe.monginfo.entity.Mong;
import com.example.damagochibe.monginfo.repository.MongInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MongInfoService {
    private final MongInfoRepo mongInfoRepo;

    public Long findMongByMember(Long memberId) {
        return mongInfoRepo.findMongByMember(memberId);
    }
    public List<Mong> getAllMongs() {
        return mongInfoRepo.findAll();
    }
    public Mong getMongById(Long id) {
        Optional<Mong> oMong = mongInfoRepo.findById(id);
        return oMong.orElse(null);
        }
    public Mong addMong(Mong mong) {
        return mongInfoRepo.save(mong);
        }
    public Mong updateMong(Long id, Mong mong) {
        Mong existingMong = mongInfoRepo.findById(id).orElse(null);
        if(existingMong == null){
            return null;
        }
        mong.setId(existingMong.getId());
        return mongInfoRepo.save(mong);
    }
    //멤버아이디로 몽을 가져오는 메소드 필요
    public Mong findMongByMemberId(Long memberId){
        return mongInfoRepo.findMongByMemberId(memberId);
    }
    public void deleteMong(Long id) {
        mongInfoRepo.deleteById(id);
    }


    public MongBattleDto getMongBattleInfo(Long id) {
        return new MongBattleDto.findById(id).orElse(null);
    }


    public MongBattleDto createMongBattleInfo(Long id, MongBattleDto mongBattleDto) {
        return null;
    }
}