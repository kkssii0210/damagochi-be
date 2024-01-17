package com.example.damagochibe.purchase.service;

import com.example.damagochibe.member.repository.MemberRepository;
import com.example.damagochibe.purchase.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;

    public void purchase(Long memberId) {
        memberRepository.findByMemberId(memberId);
    }

}
