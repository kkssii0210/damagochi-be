package com.example.damagochibe.itemFile.service;

import com.example.damagochibe.itemFile.repository.ItemFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemFileService {
    private ItemFileRepository itemFileRepository;
}
