package com.example.damagochibe.auth.repository;

import com.example.damagochibe.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    @Query("SELECT R.memberId FROM RefreshToken R WHERE R.refreshToken = :refreshToken")
    Long findMemberIdByToken(String refreshToken);
    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken R WHERE R.memberId = :memberId")
    int deleteByMemberId(Long memberId);
}
