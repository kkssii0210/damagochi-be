package com.example.damagochibe.monginfo.service;

import com.example.damagochibe.monginfo.dto.MongBattleDto;
import com.example.damagochibe.monginfo.dto.MongInfoDto;
import com.example.damagochibe.monginfo.entity.Mong;
import com.example.damagochibe.monginfo.repository.MongInfoRepo;
import com.nimbusds.jose.crypto.opts.OptionUtils;
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
        Optional<Mong> optionalMong = mongInfoRepo.findById(id);
        if(optionalMong.isPresent()){
            Mong mong = optionalMong.get();
            return new MongBattleDto(mong.getId(), mong.getWin(), mong.getLose());
        }else{return null;}
    }

    public MongBattleDto updateMongBattleInfo(Long id, MongBattleDto mongBattleDto){
        Optional<Mong> mongOptional = mongInfoRepo.findById(id);
        if(mongOptional.isPresent()){
            Mong existingMong = mongOptional.get();

            existingMong.setWin(mongBattleDto.getWin());
            existingMong.setLose(mongBattleDto.getLose());
            Mong savedMong = mongInfoRepo.save(existingMong);
            return new MongBattleDto(savedMong.getId(), savedMong.getWin(),savedMong.getLose());

        }else {
            return null;
        }
    }
}
