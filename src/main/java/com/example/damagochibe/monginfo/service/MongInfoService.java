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

    public Mong findMongByMemberId(Long memberId) {
        return mongInfoRepo.findMongByMemberId(memberId);
    }

    public void deleteMong(Long id) {
        mongInfoRepo.deleteById(id);
    }

    public MongBattleDto getMongBattleInfo(Long id) {
        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
        if (mongOptional.isPresent()) {
            Mong mong = mongOptional.get();
            MongBattleDto mongBattleDto = new MongBattleDto();
            mongBattleDto.setMongId(mong.getId());
            mongBattleDto.setWin(mong.getWin());
            mongBattleDto.setLose(mong.getLose());

            return mongBattleDto;
        } else {
            return null;
        }
    }

    public MongBattleDto updateMongBattleInfo(Long id, MongBattleDto mongBattleDto) {
        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
        if (mongOptional.isPresent()) {
            Mong existingMong = mongOptional.get();

            existingMong.setWin(mongBattleDto.getWin());
            existingMong.setLose(mongBattleDto.getLose());
            Mong updatedMong = mongInfoRepo.save(existingMong);
//            mongInfoRepo.save(existingMong);
            return convertToMongBattleDto(updatedMong);
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

//오후 4:41 2024-01-18
// getMongBattleInfo 메소드의 미구현 로직 문제 ---> return  null로 되어 있음 MongBattleDto를 반환하도록 작성해야 함
//updateBattleDto메소드의 리팩토링: 불필요하게 private메서드로 되어있음
//독립적인 유틸리티 클래스로 옮기거나 convertToMongBattleInfo 메소드를 MongInfoService 에 넣거나 하는게 좋을 듯.

