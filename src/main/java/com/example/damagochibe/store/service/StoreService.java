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
    public Page<StoreDto> foodList(Pageable pageable) {

        Page<Food> foodList = foodRepository.findAll(pageable);

        List<StoreDto> storeFoodList = foodList.getContent().stream()
                .map(food -> StoreDto.builder()
                        .storeId(food.getFoodId())
                        .itemCategory(food.getCategory())
                        .itemName(food.getFoodName())
                        .itemFunction(food.getFoodFunction())
                        .itemPrice(food.getFoodPrice())
                        .itemFileUrls(getItemFileUrls(food.getFoodId(), food.getCategory()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(storeFoodList, pageable, foodList.getTotalElements());


    }

    private List<String> getItemFileUrls(Long foodId, String category) {




        return List.of("fileurl/" + foodId + "/" + category);
    }

    public Page<StoreDto> liquidMedicineList(Pageable pageable) {
        Page<LiquidMedicine> liquidMedicineList = liquidMedicineRepository.findAll(pageable);
        System.out.println("liquidMedicineList.getContent() = " + liquidMedicineList.getContent());

        List<StoreDto> storeMedicineList = liquidMedicineList.getContent().stream()
                .map(liquidMedicine -> StoreDto.builder()
                        .storeId(liquidMedicine.getLiquidMedicineId())
                        .itemFunction(liquidMedicine.getLiquidMedicineFunction())
                        .itemName(liquidMedicine.getLiquidMedicineName())
                        .itemCategory(liquidMedicine.getCategory())
                        .itemPrice(liquidMedicine.getLiquidMedicinePrice())
                        .itemFileUrls(getItemFileUrls(liquidMedicine.getLiquidMedicineId(), liquidMedicine.getCategory()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(storeMedicineList, pageable, liquidMedicineList.getTotalElements());

    }

    public Page<StoreDto> mapList(Pageable pageable) {

        Page<Mymap> mapList = mymapRepository.findAll(pageable);
        System.out.println("mapList.getContent() = " + mapList.getContent());
        List<StoreDto> storeMapList = mapList.getContent().stream()
                .filter(myMap -> !"MP000".equals(myMap.getMapCode()))
                    .map(myMap -> StoreDto.builder()
                            .storeId(myMap.getMymapId())
                            .itemFunction(myMap.getMapFunction())
                            .itemName(myMap.getMapName())
                            .itemCategory(myMap.getCategory())
                            .itemPrice(myMap.getMapPrice())
                            .itemFileUrls(getItemFileUrls(myMap.getMymapId(), myMap.getCategory()))
                            .build())
                    .collect(Collectors.toList());

            return new PageImpl<>(storeMapList, pageable, mapList.getTotalElements());
    }

    public Food foodViewById(Long foodId) {
        // 아이템 보기
        Optional<Food> foodView = foodRepository.findById(foodId);

        if (foodView.isPresent()) {
            return foodView.get();
        }
        return null;
    }

    public LiquidMedicine liquidMedicineViewById(Long liquidMedicineId) {
        Optional<LiquidMedicine> liquidMedicineView = liquidMedicineRepository.findById(liquidMedicineId);

        if (liquidMedicineView.isPresent()) {
            return liquidMedicineView.get();
        }
        return null;
    }

    public Mymap mapViewById(Long mapId) {
        Optional<Mymap> mapView = mymapRepository.findById(mapId);

        if (mapView.isPresent()) {
            return mapView.get();
        }
        return null;
    }


    // 아이템 삭제
    public void deleteByStoreId(DeleteReqDto deleteReqDto) {
        System.out.println("deleteReqDto = " + deleteReqDto);
        String category = deleteReqDto.getCategory();
        System.out.println("category = " + category);
        // storeId와 category를 가지고 삭제

        if (category.equals("food")) {
            foodRepository.deleteById(deleteReqDto.getStoreId());
        }

        if (category.equals("liquidMedicine")) {
            liquidMedicineRepository.deleteById(deleteReqDto.getStoreId());
        }

        if (category.equals("map")) {
            mymapRepository.deleteById(deleteReqDto.getStoreId());
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
