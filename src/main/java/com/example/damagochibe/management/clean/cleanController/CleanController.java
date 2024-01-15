package com.example.damagochibe.management.clean.cleanController;

import com.example.damagochibe.management.clean.cleanService.CleanService;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/clean")
public class CleanController {

    private final CleanService cleanService;

    @PutMapping
    public ResponseEntity clean(@RequestBody Mong mong) {
        return cleanService.clean(mong);
    }
}
