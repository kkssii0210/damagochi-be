package com.example.damagochibe.store.controller;

import com.example.damagochibe.Item.food.entity.Food;
import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.auth.config.AuthConfig;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.store.dto.DeleteReqDto;
import com.example.damagochibe.store.dto.StoreDto;
import com.example.damagochibe.store.entity.Store;
import com.example.damagochibe.store.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;
    private final AuthConfig authConfig;

    @GetMapping("/accessToken")
    public ResponseEntity memberInfo(HttpServletRequest request) {
        Member loginMember = authConfig.tokenValidationService(request);

        if (loginMember != null) {

            return ResponseEntity.ok().body(loginMember);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping("/item/register")
    public ResponseEntity<Object> register(@Validated StoreDto storeDto,
                                           @RequestParam( value = "files[]", required = false) MultipartFile[] files) {
        System.out.println("storeDto = " + storeDto);

        Store store = new Store();
        if (storeDto.getItemCategory().equals("food")) {
            storeService.foodRegister(convertToFood(storeDto), files);
        } else if (storeDto.getItemCategory().equals("liquidMedicine")) {
            storeService.liquidMedicineRegister(convertToMedicine(storeDto), files);
        } else if (storeDto.getItemCategory().equals("map")) {
            storeService.mapRegister(convertToMap(storeDto), files);
        }
        return ResponseEntity.ok().body(store);
    }

    // storeDto를 Food, medicine, map Entity로 바꿔주는 메소드
    private Food convertToFood(StoreDto storeDto) {
        Food food = Food.builder()
                .storeId(storeDto.getStoreId())
                .foodName(storeDto.getItemName())
                .category(storeDto.getItemCategory())
                .foodFunction(storeDto.getItemFunction())
                .foodPrice(storeDto.getItemPrice()).build();
        System.out.println("food.getStoreId()=" + food.getStoreId()); //null
        return food;

    }

    private LiquidMedicine convertToMedicine(StoreDto storeDto) {
        LiquidMedicine liquidMedicine = LiquidMedicine.builder()
                .storeId(storeDto.getStoreId())
                .liquidMedicineName(storeDto.getItemName())
                .category(storeDto.getItemCategory())
                .liquidMedicineFunction(storeDto.getItemFunction())
                .liquidMedicinePrice(storeDto.getItemPrice()).build();

        return liquidMedicine;
    }

    private Mymap convertToMap(StoreDto storeDto) {
        Mymap map = Mymap.builder()
                .storeId(storeDto.getStoreId())
                .mapName(storeDto.getItemName())
                .category(storeDto.getItemCategory())
                .mapFunction(storeDto.getItemFunction())
                .mapPrice(storeDto.getItemPrice()).build();

        return map;
    }

    // storeId, category를 가져와야함
    @GetMapping("/itemInfo")
    public StoreDto itemInfo() {
        return storeService.itemInfo();
    }


    @GetMapping("/item/list")
    public Page<Store> itemList(Pageable pageable, @RequestParam StoreDto storeDto) {
        System.out.println("storeDto = " + storeDto.getItemFiles());
        System.out.println("storeDto.getStoreId() = " + storeDto.getStoreId());
        System.out.println("storeDto.getItemCategory() = " + storeDto.getItemCategory());

        Page<Store> storeFoodList = storeService.foodList(pageable, storeDto.getStoreId(), storeDto.getItemCategory());
        Page<Store> storeLiquidMedicineList = storeService.liquidMedicineList(pageable, storeDto.getStoreId(), storeDto.getItemCategory() );
        Page<Store> storeMapList = storeService.mapList(pageable, storeDto.getStoreId(), storeDto.getItemCategory());

        List<Page<Store>> totalList = Arrays.asList(storeFoodList, storeLiquidMedicineList, storeMapList);

        return totalList.stream()
                .flatMap(list -> list.getContent().stream())
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        content -> new PageImpl<>(content, pageable, content.size())));
    }

    @GetMapping("/item/view/id/{storeId}")
    public StoreDto itemViewById(@PathVariable("storeId") Long storeId, Pageable pageable) {
        Food food = storeService.foodViewById(storeId);
        LiquidMedicine liquidMedicine = storeService.liquidMedicineViewById(storeId);
        Mymap map = storeService.mapViewById(storeId);

        // List로 반환하지 않고 새 Store객체 만들어서 food, liquidMedicine, map 넣고 return함
        StoreDto combinedStore = new StoreDto();
        if (food != null) {
            combinedStore.setStoreId(food.getFoodId());
            combinedStore.setItemName(food.getFoodName());
            combinedStore.setItemCategory(food.getCategory());
            combinedStore.setItemFunction(food.getFoodFunction());
            combinedStore.setItemPrice(food.getFoodPrice());
        }

        if (liquidMedicine != null) {
            combinedStore.setStoreId(liquidMedicine.getLiquidMedicineId());
            combinedStore.setItemName(liquidMedicine.getLiquidMedicineName());
            combinedStore.setItemCategory(liquidMedicine.getCategory());
            combinedStore.setItemFunction(liquidMedicine.getLiquidMedicineFunction());
            combinedStore.setItemPrice(liquidMedicine.getLiquidMedicinePrice());
        }

        if (map != null) {
            combinedStore.setStoreId(map.getMymapId());
            combinedStore.setItemName(map.getMapName());
            combinedStore.setItemCategory(map.getCategory());
            combinedStore.setItemFunction(map.getMapFunction());
            combinedStore.setItemPrice(map.getMapPrice());
        }

        return combinedStore;
    }

    // 아이템 삭제하기
    @DeleteMapping("/item/delete")
    public void deleteItem(@RequestBody DeleteReqDto deleteReqDto) {
        System.out.println("deleteReqDto = " + deleteReqDto.getStoreId() + deleteReqDto.getCategory());
        storeService.deleteByStoreId(deleteReqDto);
    }

    // 편집을 위한 아이템 값 가져오기. 기존 메소드 이용
    @GetMapping("/item/id/{storeId}")
    public StoreDto item(@PathVariable Long storeId, Pageable pageable) {
        return itemViewById(storeId, pageable);
    }

    @PutMapping("/item/edit/id/{storeId}")
    public void updateItem(@PathVariable Long storeId, @RequestBody StoreDto storeDto) {
        System.out.println("storeId = " + storeId);
        System.out.println("store.getStoreId() = " + storeDto.getStoreId());
        System.out.println("store.getItemName() = " + storeDto.getItemName());
        System.out.println("storeDto.getItemCategory() = " + storeDto.getItemCategory());
        System.out.println("store.getItemFunction() = " + storeDto.getItemFunction());
        System.out.println("store.getItemPrice() = " + storeDto.getItemPrice());
        storeService.itemEdit(storeId, storeDto);
    }

}
