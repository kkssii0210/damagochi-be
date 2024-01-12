package com.example.damagochibe.member.repository;
import com.example.damagochibe.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByPlayerId(String playerId);

    Optional<Member> findByMemberId(Long memberId);
    @Query("SELECT m.playerId FROM Member m WHERE m.playerId = :playerId")
    Optional<String> findPlayerIdByPlayerId(String playerId);

    @Query("SELECT m.isSocialMember FROM Member m WHERE m.memberId = :memberId")
    Boolean checkSocialMemberByMemberId(Long memberId);
    @Query("SELECT m.playerId FROM Member m WHERE m.memberId = :memberId")
    String findPlayerIdByMemberId(Long memberId);
}
