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
        if (existingMong == null) {
            return null;
        }
        mong.setId(existingMong.getId());
        return mongInfoRepo.save(mong);
    }

    //멤버아이디로 몽을 가져오는 메소드 필요
    public Mong findMongByMemberId(Long memberId) {
        return mongInfoRepo.findMongByMemberId(memberId);
    }

    public void deleteMong(Long id) {
        mongInfoRepo.deleteById(id);
    }
    //몽 객체를 BattleDto로 변환하는 로직 구현
    //    몽의 배틀 정보를 업데이트하는 메소드
    public MongBattleDto getMongBattleInfo(Long id) {
        return null;
    }

    public MongBattleDto updateMongBattleInfo(Long id, MongBattleDto mongBattleDto) {
        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
        if (mongOptional.isPresent()) {
            Mong existingMong = mongOptional.get();

            existingMong.setWin(mongBattleDto.getWin());
            existingMong.setLose(mongBattleDto.getLose());

            mongInfoRepo.save(existingMong);
            return convertToMongBattleDto(existingMong);
        } else {
            return null;
        }
    }

    private MongBattleDto convertToMongBattleDto(Mong mong) {
        MongBattleDto mongBattleDto = new MongBattleDto();
        mongBattleDto.setMongId(mong.getId());
        mongBattleDto.setWin(mong.getWin());
        mongBattleDto.setLose(mong.getLose());
        return mongBattleDto;
    }
}

