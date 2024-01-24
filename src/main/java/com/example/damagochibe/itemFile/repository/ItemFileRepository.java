package com.example.damagochibe.itemFile.repository;

import com.example.damagochibe.itemFile.entity.ItemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemFileRepository extends JpaRepository<ItemFile, Long> {

    @Query("SELECT i FROM ItemFile i WHERE i.storeId = :storeId AND i.category = :itemCategory")
    List<ItemFile> findByStoreIdAndCategory(Long storeId, String itemCategory);

}
