package com.example.damagochibe.Item.food.service;

import com.example.damagochibe.Item.food.repository.FoodRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FoodService {
    private FoodRepository foodRepository;


}
