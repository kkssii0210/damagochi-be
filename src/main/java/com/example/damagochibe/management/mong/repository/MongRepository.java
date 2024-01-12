package com.example.damagochibe.management.mong.repository;

import com.example.damagochibe.monginfo.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongRepository extends JpaRepository<Mong, Long> {
    Mong findByMemberId(String memberId);

}
