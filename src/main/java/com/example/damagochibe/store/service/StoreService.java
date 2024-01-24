package com.example.damagochibe.store.service;

import com.example.damagochibe.Item.food.entity.Food;
import com.example.damagochibe.Item.food.repository.FoodRepository;
import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import com.example.damagochibe.Item.liquidMedicine.repository.LiquidMedicineRepository;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.itemFile.dto.ItemFileDto;
import com.example.damagochibe.itemFile.entity.ItemFile;
import com.example.damagochibe.itemFile.repository.ItemFileRepository;
import com.example.damagochibe.store.dto.DeleteReqDto;
import com.example.damagochibe.store.dto.StoreDto;
import com.example.damagochibe.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
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
    private final ItemFileRepository itemFileRepository;


    @Value("${image.file.prefix}")
    private String imageUrlPrefix;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    private final S3Client s3;

    //AWS s3에 파일 업로드
    private void upload(Long storeId, String category, MultipartFile file) {

        try {
            String key = "damagochi/" + storeId + "/"+ category + "/" + file.getOriginalFilename();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 아이템 등록
    public void foodRegister(Food food, MultipartFile[] files) {
        Food savedFood = foodRepository.save(food);

        if (files != null) {
            for (MultipartFile file : files) {
                // ItemFile DB에 food.getFoodId()와 file이름을 저장
                System.out.println("서비스에서 savedFood.getFoodId() = " + savedFood.getFoodId());
                ItemFile itemFile = new ItemFile();
                itemFile.setStoreId(savedFood.getFoodId());
                itemFile.setCategory(savedFood.getCategory());
                itemFile.setFileName(file.getOriginalFilename());
                itemFileRepository.save(itemFile);

                upload(itemFile.getStoreId(), food.getCategory(), file);
            }
        }
    }

    public void liquidMedicineRegister(LiquidMedicine liquidMedicine, MultipartFile[] files) {
        LiquidMedicine savedMedicine = liquidMedicineRepository.save(liquidMedicine);

        if (files != null) {
            for (MultipartFile file : files) {
                // ItemFile DB에 food.getFoodId()와 file이름을 저장
                System.out.println("서비스에서 savedMedicine.getLiquidMedicineId() = " + savedMedicine.getLiquidMedicineId());
                ItemFile itemFile = new ItemFile();
                itemFile.setStoreId(savedMedicine.getLiquidMedicineId());
                itemFile.setCategory(savedMedicine.getCategory());
                itemFile.setFileName(file.getOriginalFilename());
                itemFileRepository.save(itemFile);

                upload(itemFile.getStoreId(), itemFile.getCategory(), file);
            }
        }
    }

    public void mapRegister(Mymap map, MultipartFile[] files) {
        Mymap savedMap = mymapRepository.save(map);

        if (files != null) {
            for (MultipartFile file : files) {
                // ItemFile DB에 food.getFoodId()와 file이름을 저장
                System.out.println("서비스에서 savedMedicine.getLiquidMedicineId() = " + savedMap.getMymapId());
                ItemFile itemFile = new ItemFile();
                itemFile.setStoreId(savedMap.getMymapId());
                itemFile.setCategory(savedMap.getCategory());
                itemFile.setFileName(file.getOriginalFilename());
                itemFileRepository.save(itemFile);

                upload(itemFile.getStoreId(), itemFile.getCategory(), file);
            }
        }
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
                        .itemFiles(getItemFileUrls(food.getFoodId(), food.getCategory()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(storeFoodList, pageable, foodList.getTotalElements());


    }

    private List<ItemFileDto> getItemFileUrls(Long storeId, String category) {

        List<ItemFile> itemFiles = itemFileRepository.findByStoreIdAndCategory(storeId, category);

        // https://study0210.s3.ap-northeast-2.amazonaws.com/damagochi/24/liquidMedicine/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-01-18%20123606.png

        List<ItemFileDto> result = itemFiles.stream().map(e -> ItemFileDto.builder()
                .id(e.getId())
                .fileName(e.getFileName())
//                .storeId(e.getStoreId())
//                .category(e.getCategory())
                .fileUrl(imageUrlPrefix + "damagochi/" + storeId + "/" + category + "/" + e.getFileName())
                .build())
                .toList();

        return result;
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
                        .itemFiles(getItemFileUrls(liquidMedicine.getLiquidMedicineId(), liquidMedicine.getCategory()))
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
                            .itemFiles(getItemFileUrls(myMap.getMymapId(), myMap.getCategory()))
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
