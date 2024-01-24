package com.example.damagochibe.mongList.repository;

import com.example.damagochibe.mongList.entity.MongList;
import com.example.damagochibe.monginfo.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongListRepository extends JpaRepository<MongList, Long> {
    MongList findByMongCode(String code);
}
