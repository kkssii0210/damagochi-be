package com.example.damagochibe.cart.repository;

import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT m FROM Member m WHERE m.playerId = :playerId")
    Member findMemberByPlayerId(String playerId);

    @Query("SELECT m.memberId FROM Member m WHERE m.playerId = :playerId")
    String findMemberId(Long playerId);

    @Query("SELECT c FROM Cart c WHERE c.playerId = :playerId")
    List<Cart> findCartByPlayerId(String playerId);

    @Query("SELECT c FROM Cart c WHERE c.playerId = :playerId AND c.cartItemName = :cartItemName")
    Cart findCartByPlayerIdAndAndCartItemName(String playerId, String cartItemName);


}
