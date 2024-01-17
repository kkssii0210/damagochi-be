package com.example.damagochibe.Item.mapBackground.background.repository;

import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MymapRepository extends JpaRepository<Mymap,Long> {
    Optional<Mymap> getByMemberId(Long memberId);

}
