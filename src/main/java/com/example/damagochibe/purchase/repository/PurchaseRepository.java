package com.example.damagochibe.purchase.repository;

import com.example.damagochibe.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p WHERE p.memberId = :memberId AND p.itemName = :itemName")
    Optional<Purchase> findByMemberIdAndItemName(Long memberId, String itemName);
}

