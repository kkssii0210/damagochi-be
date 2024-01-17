package com.example.damagochibe.management.stroke.controller;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import com.example.damagochibe.management.global.cooldown.repository.CooldownRepository;
import com.example.damagochibe.management.stroke.service.StrokeService;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/stroke")
public class StrokeController {

    private final StrokeService strokeService;
    private final CooldownRepository cooldownRepository;

    @PutMapping
    public ResponseEntity stroke(@RequestBody Mong mong) {
        return strokeService.stroke(mong);
    }

    @PostMapping("/strokeCool")
    private void strokeCood(@RequestBody Mong mong) {
        strokeService.strokeCool(mong);

    }
}
