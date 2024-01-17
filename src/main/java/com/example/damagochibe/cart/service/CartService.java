package com.example.damagochibe.cart.service;

import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.repository.CartRepository;
import com.example.damagochibe.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
   private final CartRepository cartRepository;

   //playerId로 멤버 정보를 가져옴
   public Member findMember(String playerId) {
      Member member = cartRepository.findMemberByPlayerId(playerId);
      System.out.println("member = " + member);
      return member;
   }

   public Cart findCartById(String playerId) {
      return cartRepository.findCartByPlayerId(playerId);
   }


   public void findMemberId(Long playerId) {
      cartRepository.findMemberId(playerId);
   }


//   public void addCart(String playerId) {
//      cartRepository.
//   }
}
