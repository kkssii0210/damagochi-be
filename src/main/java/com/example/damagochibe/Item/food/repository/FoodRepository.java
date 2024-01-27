package com.example.damagochibe.Item.food.repository;

import com.example.damagochibe.Item.food.dto.FoodDto;
import com.example.damagochibe.Item.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("SELECT f FROM Food f WHERE f.foodName = :itemName")
    Food findByName(String itemName);
}
