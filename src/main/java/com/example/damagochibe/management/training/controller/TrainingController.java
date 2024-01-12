package com.example.damagochibe.management.training.controller;

import com.example.damagochibe.management.training.service.TrainingService;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/training")
public class TrainingController {

    private final TrainingService trainingService;

    @PutMapping
    public ResponseEntity training(@RequestBody Mong mong) {
        return trainingService.training(mong);
    }

    @PostMapping("/trainingCool")
    private void trainingCool(@RequestBody Mong mong) {
        trainingService.trainingCool(mong);
    }
}
