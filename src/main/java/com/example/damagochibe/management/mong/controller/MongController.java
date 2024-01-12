package com.example.damagochibe.management.mong.controller;

import com.example.damagochibe.auth.security.CustomUserDetail;
import com.example.damagochibe.management.mong.service.MongService1;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/mong")
public class MongController {

    private final MongService1 mongService1;

    @GetMapping
    public Mong getInfo(@AuthenticationPrincipal CustomUserDetail principal) {
        System.out.println("principal.getUsername() = " + principal.getUsername());
        return mongService1.getInfo(principal.getUsername());
    }

    @PutMapping("/levelUp")
    public ResponseEntity levelUp(@RequestBody Mong mong) {
        return mongService1.levelUp(mong.getMemberId());
    }
}
