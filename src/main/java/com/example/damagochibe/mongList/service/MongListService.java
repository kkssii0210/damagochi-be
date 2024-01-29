package com.example.damagochibe.mongList.service;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.mong.repository.MongRepository;
import com.example.damagochibe.mongList.entity.MongList;
import com.example.damagochibe.mongList.repository.MongListRepository;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongListService {

    private final MongListRepository mongListRepository;
    private final MongRepository mongRepository;
    private final CooldownRepository cooldownRepository;
    public Mong getMong(String username, String mongName) {
        String code = "00";
        code += ((int) (Math.random() * 3) + 1);
        System.out.println("code = " + code);
        MongList myMong = mongListRepository.findByMongCode(code);
        System.out.println("myMong = " + myMong.toString());
        System.out.println("myMong = " + myMong.getAttribute());

        Mong mong = new Mong();
        mong.setName(mongName);
        mong.setMongCode(myMong.getMongCode());
        mong.setStrength(myMong.getStrength());
        mong.setAgility(myMong.getAgility());
        mong.setDefense(myMong.getDefense());
        mong.setHealth(myMong.getHealth());
        mong.setAttribute(myMong.getAttribute());
        mong.setMemberId(username);
        mong.setClean(true);
        mongRepository.save(mong);

        Cooldown cooldown = new Cooldown();
        cooldown.setMong(mong);
        cooldownRepository.save(cooldown);

        return mong;
    }
}
