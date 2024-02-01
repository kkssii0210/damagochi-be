package com.example.damagochibe.inventory.repository;

import com.example.damagochibe.inventory.enetity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findAllByMemberId(String memberId);

    @Query("SELECT i FROM Inventory i WHERE i.memberId = :memberId AND i.name = :itemName")
    Optional<Inventory> findByMemberIdAndItemName(Long memberId, String itemName);
}
