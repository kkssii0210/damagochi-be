package com.example.damagochibe.store.service;

import com.example.damagochibe.Item.food.entity.Food;
import com.example.damagochibe.Item.food.repository.FoodRepository;
import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import com.example.damagochibe.Item.liquidMedicine.repository.LiquidMedicineRepository;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.store.dto.DeleteReqDto;
import com.example.damagochibe.store.dto.StoreDto;
import com.example.damagochibe.store.entity.Store;
import com.example.damagochibe.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final FoodRepository foodRepository;
    private final LiquidMedicineRepository liquidMedicineRepository;
    private final MymapRepository mymapRepository;

    // 아이템 등록
    public Food foodRegister(Food food) {
        return foodRepository.save(food);
    }

    public LiquidMedicine liquidMedicineRegister(LiquidMedicine liquidMedicine) {
        return liquidMedicineRepository.save(liquidMedicine);
    }

    public Mymap mapRegister(Mymap map) {
        return mymapRepository.save(map);
    }


    // 아이템 리스트 가져오기
    public Page<Store> foodList(Pageable pageable) {

        Page<Food> foodList = foodRepository.findAll(pageable);

        List<Store> storeFoodList = foodList.getContent().stream()
                .map(food -> Store.builder()
                        .storeId(food.getFoodId())
                        .itemCategory(food.getCategory())
                        .itemName(food.getFoodName())
                        .itemFunction(food.getFoodFunction())
                        .itemCategory(food.getCategory())
                        .itemPrice(food.getFoodPrice())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(storeFoodList, pageable, foodList.getTotalElements());


    }

    public Page<Store> liquidMedicineList(Pageable pageable) {
        Page<LiquidMedicine> liquidMedicineList = liquidMedicineRepository.findAll(pageable);

        List<Store> storeMedicineList = liquidMedicineList.getContent().stream()
                .map(liquidMedicine -> Store.builder()
                        .storeId(liquidMedicine.getLiquidMedicineId())
                        .itemName(liquidMedicine.getLiquidMedicineName())
                        .itemFunction(liquidMedicine.getLiquidMedicineFunction())
                        .itemCategory(liquidMedicine.getCategory())
                        .itemPrice(liquidMedicine.getLiquidMedicinePrice())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(storeMedicineList, pageable, liquidMedicineList.getTotalElements());

    }

    public Page<Store> mapList(Pageable pageable) {
        Page<Mymap> mapList = mymapRepository.findAll(pageable);

        List<Store> storeMapList = mapList.getContent().stream()
                .map(myMap -> Store.builder()
                        .storeId(myMap.getMymapId())
                        .itemName(myMap.getMapName())
                        .itemFunction(myMap.getMapFunction())
                        .itemCategory(myMap.getCategory())
                        .itemPrice(myMap.getMapPrice())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(storeMapList, pageable, mapList.getTotalElements());
    }

    // 아이템 보기
    public Store foodViewById(Long foodId) {
        Optional<Food> foodView = foodRepository.findById(foodId);
        System.out.println("foodView = " + foodView);

        Store foods = null;
        if (foodView.isPresent()) {
            foods = Store.builder()
                    .storeId(foodView.get().getFoodId())
                    .itemName(foodView.get().getFoodName())
                    .itemCategory(foodView.get().getCategory())
                    .itemFunction(foodView.get().getFoodFunction())
                    .itemPrice(foodView.get().getFoodPrice()).build();
        }

        return foods;
    }

    public Store liquidMedicineViewById(Long liquidMedicineId) {
        Optional<LiquidMedicine> liquidMedicineView = liquidMedicineRepository.findById(liquidMedicineId);
        System.out.println("liquidMedicineView = " + liquidMedicineView);

        Store liquidMedicines = null;
        if (liquidMedicineView.isPresent()) {
            liquidMedicines = Store.builder()
                    .storeId(liquidMedicineView.get().getLiquidMedicineId())
                    .itemName(liquidMedicineView.get().getLiquidMedicineName())
                    .itemCategory(liquidMedicineView.get().getCategory())
                    .itemFunction(liquidMedicineView.get().getLiquidMedicineFunction())
                    .itemPrice(liquidMedicineView.get().getLiquidMedicinePrice()).build();
        }

        return liquidMedicines;
    }

    public Store mapViewById(Long mapId) {
        Optional<Mymap> mapView = mymapRepository.findById(mapId);
        System.out.println("mapView = " + mapView);

        Store maps = null;
        if (mapView.isPresent()) {
            maps = Store.builder()
                    .storeId(mapView.get().getMymapId())
                    .itemName(mapView.get().getMapName())
                    .itemCategory(mapView.get().getCategory())
                    .itemFunction(mapView.get().getMapFunction())
                    .itemPrice(mapView.get().getMapPrice()).build();
        }

        return maps;
    }


    // 아이템 삭제
    public void deleteByStoreId(DeleteReqDto deleteReqDto) {
        System.out.println("deleteReqDto = " + deleteReqDto);
        String category = deleteReqDto.getCategory();
        System.out.println("category = " + category);
        // storeId와 category를 가지고 삭제

        if (category.equals("food")) {

            Long foodId = foodRepository.findFoodIdByStoreId(deleteReqDto.getStoreId());
            System.out.println("foodId = " + foodId);
            foodRepository.deleteById(foodId);
        }

        if (category.equals("liquidMedicine")) {
            Long medicineId = liquidMedicineRepository.findMedicineIdByStoreId(deleteReqDto.getStoreId());
            liquidMedicineRepository.deleteById(medicineId);
        }

        if(category.equals("map")) {
            Long mapId = mymapRepository.findMapIdByStoreId(deleteReqDto.getStoreId());
            mymapRepository.deleteById(mapId);
        }


    }

    // 아이템 수정
    public void itemEdit(Long storeId, StoreDto storeDto) {

        Optional<Food> foodContent = foodRepository.findById(storeId);
        Optional<LiquidMedicine> liquidMedicineContent = liquidMedicineRepository.findById(storeId);
        Optional<Mymap> mapContent = mymapRepository.findById(storeId);

        if (foodContent.isPresent()) {
            Food food = foodContent.get();
            food.setFoodName(storeDto.getItemName());
            food.setCategory(storeDto.getItemCategory());
            food.setFoodFunction(storeDto.getItemFunction());
            food.setFoodPrice(storeDto.getItemPrice());

            foodRepository.save(food); // 저장되는 곳은 푸드

        } else if (liquidMedicineContent.isPresent()) {
            LiquidMedicine medicine = liquidMedicineContent.get();
            medicine.setLiquidMedicineName(storeDto.getItemName());
            medicine.setCategory(storeDto.getItemCategory());
            medicine.setLiquidMedicineFunction(storeDto.getItemFunction());
            medicine.setLiquidMedicinePrice(storeDto.getItemPrice());

            liquidMedicineRepository.save(medicine);

        } else if (mapContent.isPresent()) {
            Mymap map = mapContent.get();
            map.setMapName(storeDto.getItemName());
            map.setCategory(storeDto.getItemCategory());
            map.setMapFunction(storeDto.getItemFunction());
            map.setMapPrice(storeDto.getItemPrice());

            mymapRepository.save(map);
        }
    }

}