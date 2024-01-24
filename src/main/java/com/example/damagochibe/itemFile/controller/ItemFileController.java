package com.example.damagochibe.itemFile.controller;

import com.example.damagochibe.itemFile.service.ItemFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/file")
public class ItemFileController {
    private ItemFileService itemFileService;


}
