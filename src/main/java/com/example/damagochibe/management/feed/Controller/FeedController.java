package com.example.damagochibe.management.feed.Controller;

import com.example.damagochibe.management.feed.service.FeedService;
import com.example.damagochibe.monginfo.entity.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manage/feed")
public class FeedController {

    private final FeedService feedService;

    @PutMapping
    public ResponseEntity feed(@RequestBody Mong mong) {
        return feedService.feed(mong);
    }

    @PostMapping("/feedCool")
    public void feedCool(@RequestBody Mong mong) {
        feedService.feedCool(mong);
//        feedService.feedDirty(mong);
    }
}
