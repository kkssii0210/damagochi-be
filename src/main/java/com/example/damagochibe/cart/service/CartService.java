package com.example.damagochibe.cart.service;

import com.example.damagochibe.Item.food.entity.Food;
import com.example.damagochibe.Item.food.repository.FoodRepository;
import com.example.damagochibe.Item.liquidMedicine.entity.LiquidMedicine;
import com.example.damagochibe.Item.liquidMedicine.repository.LiquidMedicineRepository;
import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.cart.dto.CartReqDto;
import com.example.damagochibe.cart.entity.Cart;
import com.example.damagochibe.cart.repository.CartRepository;
import com.example.damagochibe.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final FoodRepository foodRepository;
    private final LiquidMedicineRepository liquidMedicineRepository;
    private final MymapRepository mapRepository;

    // 카트에 아이템 담기
    public Cart addCart(CartReqDto cartReqDto) {
        System.out.println("cartReqDto.getPlayerId() = " + cartReqDto.getPlayerId());
        System.out.println("cartReqDto.getStoreId() = " + cartReqDto.getStoreId());
        System.out.println("cartReqDto.getCategory() = " + cartReqDto.getCategory());
        System.out.println("cartReqDto.getItemCount() = " + cartReqDto.getItemCount());
        System.out.println("cartReqDto.getItemName() = " + cartReqDto.getItemName());

        // 저장할 때 같은 이름이 있다면, 새로 저장되는게 아닌 기존 수량에서 + 되도록 해야함. 없다면 새로 저장
        String itemName = cartReqDto.getItemName();
        String playerId = cartReqDto.getPlayerId();
        Cart item = cartRepository.findCartByPlayerIdAndAndCartItemName(playerId, itemName); // 주의: 매개변수 순서 지키기

        // 아예 없다면 수량 뿐만 아니라 모두  save해야함
        if (item == null) {
            Cart newCartItem = createCartItem(cartReqDto);
            return cartRepository.save(newCartItem);
        } else {
            System.out.println("item.getCartItemCount() = " + item.getCartItemCount());
            Integer oldCount = item.getCartItemCount();
            int newCount = oldCount + cartReqDto.getItemCount();
            System.out.println("newCount 찍어보기 = " + newCount);
            item.setCartItemCount(newCount);
            System.out.println("setItemCount 후 추가된 newCount = " + newCount);

            return cartRepository.save(item);
        }
    }

    private Cart createCartItem(CartReqDto cartReqDto) {
        //분류가 food면 카트 리포지토리에 category 푸드와, 멤버의 playerId 저장
        // food이면서 storeId(foodId)를 만족해야함
        if (cartReqDto.getCategory().equals("food")) {

            Optional<Food> foodInfo = foodRepository.findById(cartReqDto.getStoreId());
            Food food = foodInfo.get();
            Cart cart = Cart.builder()
                    .playerId(cartReqDto.getPlayerId())
                    .cartItemCategory(food.getCategory())
                    .cartItemName(food.getFoodName())
                    .cartItemCount(cartReqDto.getItemCount())
                    .cartItemPrice(food.getFoodPrice()).build();
            return cartRepository.save(cart);
        }

        if (cartReqDto.getCategory().equals("liquidMedicine")) {
            Optional<LiquidMedicine> liquidMedicineInfo = liquidMedicineRepository.findById(cartReqDto.getStoreId());
            LiquidMedicine liquidMedicine = liquidMedicineInfo.get();
            Cart cart = Cart.builder()
                    .playerId(cartReqDto.getPlayerId())
                    .cartItemCategory(liquidMedicine.getCategory())
                    .cartItemName(liquidMedicine.getLiquidMedicineName())
                    .cartItemCount(cartReqDto.getItemCount())
                    .cartItemPrice(liquidMedicine.getLiquidMedicinePrice()).build();
            return cartRepository.save(cart);
        }

        if (cartReqDto.getCategory().equals("map")) {
            Optional<Mymap> mapInfo = mapRepository.findById(cartReqDto.getStoreId());
            Mymap map = mapInfo.get();
            Cart cart = Cart.builder()
                    .playerId(cartReqDto.getPlayerId())
                    .cartItemCategory(map.getCategory())
                    .cartItemName(map.getMapName())
                    .cartItemCount(cartReqDto.getItemCount())
                    .cartItemPrice(map.getMapPrice()).build();
            return cartRepository.save(cart);
        }
        return null;
    }


    // 카트에 담은 아이템 정보 불러오기
    public List<Cart> getCartItem(String playerId) {
        System.out.println("playerId Service에서 = " + playerId);
        // playerId를 통해 cart정보를 불러옴
        return cartRepository.findCartByPlayerId(playerId);
    }

    public void deleteCartItem(CartReqDto cartReqDto) {
        System.out.println("cartReqDto.getItemName() = " + cartReqDto.getItemName());
        System.out.println("cartReqDto.getPlayerId() = " + cartReqDto.getPlayerId());

        String itemName = cartReqDto.getItemName();
        String playerId = cartReqDto.getPlayerId();

        Long cartId = cartRepository.findCartIdByItemNameAndPlayerId(itemName, playerId);
        System.out.println("삭제 하려는 cartId = " + cartId);
        cartRepository.deleteById(cartId);
    }
}

