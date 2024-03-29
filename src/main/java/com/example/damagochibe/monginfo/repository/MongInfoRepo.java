package com.example.damagochibe.monginfo.repository;

import com.example.damagochibe.monginfo.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongInfoRepo extends JpaRepository<Mong, Long> {
    @Query("SELECT m FROM Mong m WHERE m.memberId = :memberId")
    Mong findMongByMemberId(String memberId);
    @Query("SELECT m.id FROM Mong m WHERE m.memberId = :memberId")
    Long findMongByMember(Long memberId);
    @Query("SELECT m.id from Mong m WHERE m.memberId = :memberId")
    Long findMongByPlayerId(String memberId);
}