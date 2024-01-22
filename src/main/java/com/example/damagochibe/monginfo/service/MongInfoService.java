package com.example.damagochibe.monginfo.service;

import com.example.damagochibe.monginfo.dto.MongBattleDto;
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
//    private final MongBattleDto mongBattleDto;

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
        Optional<Mong> optionalMong = mongInfoRepo.findById(id);
        if (optionalMong.isPresent()) {
            Mong mong = optionalMong.get();
            return new MongBattleDto(mong.getId(), mong.getWin(), mong.getLose());
        } else {
            return null;
        }
    }
//    주어진 아이디에 따라 몽을 조회하고 몽의 정보를 가져와서 몽배틀디티오객체로 변환하여 반환하는 메솓,

//    public MongBattleDto updateMongBattleInfo(Long id) {
//        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
//        if (mongOptional.isPresent()) {
//            Mong existingMong = mongOptional.get();
//            if (existingMong.getWin() > 0 ){
//                existingMong.setWin(existingMong.getWin()+ 1);
//            }else {
//                existingMong.setLose(existingMong.getLose()+ 1);
//            }
//            Mong savedMong = mongInfoRepo.save(existingMong);
//            return new MongBattleDto(savedMong.getId(), savedMong.getWin(), savedMong.getLose());
//
//        } else {
//            return null;
//        }
//    }
//
//    public MongBattleDto updateGetWin(Long id) {
//        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
//        //아이디에 해당하는 몽을 조회 함
//        //몽이 존재하면 실행될 코드
//        if (mongOptional.isPresent()) {
//            Mong existinMong = mongOptional.get();
//            existinMong.setWin(existinMong.getWin() + 1);
//            Mong savedMong = mongInfoRepo.save(existingMong);
//            return new MongBattleDto(savedMong.getId(), savedMong.getWin(), savedMong.getLose());
//        } else {
//            return null;
//        }
//    }

    public MongBattleDto updateWin(Long id) {
        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
        if (mongOptional.isPresent()) {
            Mong existinMong = mongOptional.get();
            existinMong.setWin(existinMong.getWin() + 1);
            Mong savedMong = mongInfoRepo.save(existinMong);
            return new MongBattleDto(savedMong.getId(), savedMong.getWin(), savedMong.getLose());
        } else {
            return null;
        }
    }
    public MongBattleDto updateLose(Long id) {
        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
        if (mongOptional.isPresent()) {
            Mong existingMong = mongOptional.get();
            existingMong.setLose(existingMong.getLose() + 1);
            Mong savedMong = mongInfoRepo.save(existingMong);
            return new MongBattleDto(savedMong.getId(), savedMong.getWin(), savedMong.getLose());
        } else {
            return null;
        }
    }
}