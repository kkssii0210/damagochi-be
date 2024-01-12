package com.example.damagochibe.Store.controller;

import com.example.damagochibe.Item.food.entity.Food;
import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Store.dto.StoreDto;
import com.example.damagochibe.Store.entity.Store;
import com.example.damagochibe.Store.service.StoreService;
import com.sun.jdi.LongValue;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/item/register")
    public void register(@RequestBody StoreDto storeDto) {
        System.out.println("storeDto = " + storeDto);

        if (storeDto.getItemCategory().equals("food")) {
            Food food = storeService.foodRegister(convertToFood(storeDto));
        } else if (storeDto.getItemCategory().equals("liquidMedicine")) {
            storeService.liquidMedicineRegister(convertToMedicine(storeDto));
        } else if (storeDto.getItemCategory().equals("map")) {
            storeService.mapRegister(convertToMap(storeDto));
        }
    }

    // storeDto를 Food, medicine, map Entity로 바꿔주는 메소드
    private Food convertToFood(StoreDto storeDto) {
        Food food = Food.builder()
                .foodName(storeDto.getItemName())
                .foodFunction(storeDto.getItemFunction())
                .foodPrice(storeDto.getItemPrice()).build();

        return food;
    }

    private LiquidMedicine convertToMedicine(StoreDto storeDto) {
        LiquidMedicine liquidMedicine = LiquidMedicine.builder()
                .liquidMedicineName(storeDto.getItemName())
                .liquidMedicineFunction(storeDto.getItemFunction())
                .liquidMedicinePrice(storeDto.getItemPrice()).build();

        return liquidMedicine;
    }

    private Mymap convertToMap(StoreDto storeDto) {
        Mymap map = Mymap.builder()
                .mapName(storeDto.getItemName())
                .mapFunction(storeDto.getItemFunction())
                .mapPrice(storeDto.getItemPrice()).build();

        return map;
    }

    @GetMapping("/item/list")
    public Page<Store> itemList(Pageable pageable) {

        Page<Store> storeFoodList = storeService.foodList(pageable);
        Page<Store> storeLiquidMedicineList = storeService.liquidMedicineList(pageable);
        Page<Store> storeMapList = storeService.mapList(pageable);

        List<Page<Store>> totalList = Arrays.asList(storeFoodList, storeLiquidMedicineList, storeMapList);

        return totalList.stream()
                .flatMap(list -> list.getContent().stream())
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        content -> new PageImpl<>(content, pageable, content.size())));
    }

    @GetMapping("/item/view/id/{storeId}")
    public Store itemViewById(@PathVariable("storeId") Long storeId, Pageable pageable) {

        Store food = storeService.foodViewById(storeId);
        Store liquidMedicine = storeService.liquidMedicineViewById(storeId);
        Store map = storeService.mapViewById(storeId);

        // List로 반환하지 않고 새 Store객체 만들어서 food, liquidMedicine, map 넣고 return함
        Store combinedStore = new Store();
        if (food != null) {
            combinedStore.setStoreId(food.getStoreId());
            combinedStore.setItemName(food.getItemName());
            combinedStore.setItemCategory(food.getItemCategory());
            combinedStore.setItemFunction(food.getItemFunction());
            combinedStore.setItemPrice(food.getItemPrice());
        }

        if (liquidMedicine != null) {
            combinedStore.setStoreId(liquidMedicine.getStoreId());
            combinedStore.setItemName(liquidMedicine.getItemName());
            combinedStore.setItemCategory(liquidMedicine.getItemCategory());
            combinedStore.setItemFunction(liquidMedicine.getItemFunction());
            combinedStore.setItemPrice(liquidMedicine.getItemPrice());
        }

        if (map != null) {
            combinedStore.setStoreId(map.getStoreId());
            combinedStore.setItemName(map.getItemName());
            combinedStore.setItemCategory(map.getItemCategory());
            combinedStore.setItemFunction(map.getItemFunction());
            combinedStore.setItemPrice(map.getItemPrice());
        }

        return combinedStore;
    }

    // 아이템 삭제하기
    @DeleteMapping("/item/delete/{storeId}")
    public void deleteItem(@PathVariable Long storeId) {
        storeService.deleteByStoreId(storeId);
    }

    // 편집을 위한 아이템 값 가져오기. 기존 메소드 이용
    @GetMapping("/item/id/{storeId}")
    public Store item(@PathVariable Long storeId, Pageable pageable) {
        return itemViewById(storeId, pageable);
    }

    @PutMapping("/item/edit/id/{storeId}")
    public void updateItem(@PathVariable Long storeId, @RequestBody StoreDto storeDto) {
        //만약 category가 분류되어 나오면 foodRepository에만 저장할 수 있음.
        System.out.println("storeId = " + storeId);
        System.out.println("store.getStoreId() = " + storeDto.getStoreId());
        System.out.println("store.getItemName() = " + storeDto.getItemName());
        System.out.println("store.getItemFunction() = " + storeDto.getItemFunction());
        System.out.println("store.getItemPrice() = " + storeDto.getItemPrice());
        storeService.itemEdit(storeId, storeDto);
    }



}
