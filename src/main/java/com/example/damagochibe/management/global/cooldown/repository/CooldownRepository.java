package com.example.damagochibe.management.global.cooldown.repository;

import com.example.damagochibe.management.global.cooldown.entity.Cooldown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CooldownRepository extends JpaRepository<Cooldown, Long> {

    Cooldown findByMongId(Long mongId);

}
