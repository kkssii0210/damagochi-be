package com.example.damagochibe.inventory.repository;

import com.example.damagochibe.inventory.enetity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findAllByMemberId(String memberId);
}
