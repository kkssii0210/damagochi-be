package com.example.damagochibe.Item.liquidMedicine.repository;

import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface LiquidMedicineRepository extends JpaRepository<LiquidMedicine, Long> {

}
