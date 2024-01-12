package com.example.damagochibe.management.sleep.controller;

import com.example.damagochibe.management.sleep.service.SleepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/sleep")
public class SleepController {

    private final SleepService sleepService;

    @PutMapping
    public ResponseEntity sleep(@RequestBody String memberId) {
        return sleepService.sleep("hr");
    }

}
