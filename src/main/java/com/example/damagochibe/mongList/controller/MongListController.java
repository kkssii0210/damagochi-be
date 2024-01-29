package com.example.damagochibe.mongList.controller;

import com.example.damagochibe.auth.security.CustomUserDetail;
import com.example.damagochibe.mongList.service.MongListService;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mongList")
public class MongListController {

    private final MongListService mongListService;

    @PostMapping
    public Mong getMong(@AuthenticationPrincipal CustomUserDetail principal,
                        @RequestParam String mongName) {
        System.out.println("principal.getUsername() = " + principal.getUsername());
        System.out.println("mongName = " + mongName);
        return mongListService.getMong(principal.getUsername(), mongName);
    }

}
