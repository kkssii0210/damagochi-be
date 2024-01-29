package com.example.damagochibe.Item.food.repository;

import com.example.damagochibe.Item.food.dto.FoodDto;
import com.example.damagochibe.Item.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {
//    @Query("SELECT f FROM Food f WHERE f.foodName = :itemName AND f.memberId = :memberId")
//    Optional<Food> findByItemNameAndMemberId(String itemName, Long memberId);
}
