package com.example.damagochibe.management.stroke.controller;

import com.example.damagochibe.management.stroke.service.StrokeService;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/stroke")
public class StrokeController {

    private final StrokeService strokeService;

    @PutMapping
    public ResponseEntity stroke(@RequestBody Mong mong) {
        return strokeService.stroke(mong);
    }

    @PostMapping("/strokeCool")
    private void strokeCood() {
        strokeService.strokeCool();
    }
}
