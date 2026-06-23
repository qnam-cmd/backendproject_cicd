package org.spring.backendprojectex.shop.advice;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.shop.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalCartControllerAdvice {

    private final CartService cartService;
    // 1. 상품 리스트의 갯수 (상품 종류 수)
    @ModelAttribute("cartCount")
    public int getCartItemCount(Authentication authentication) {
        Long memberId = extractMemberId(authentication);
        if (memberId == null) return 0;

        // 리스트를 통째로 가져와서 .size()를 하거나,
        // 혹은 전용 count 쿼리(cartService.countCartItems)를 호출합니다.
        List<ItemListDto> itemListDtos = cartService.cartList(memberId);
        if (itemListDtos == null) return 0;

        return itemListDtos.size(); //
    }

    // 2. 총 상품 갯수 (모든 상품의 수량 합계)
    @ModelAttribute("itemListCount")
    public int getItemListCount(Authentication authentication) {
        Long memberId = extractMemberId(authentication);
        if (memberId == null) return 0;

        List<ItemListDto> itemListDtos = cartService.cartList(memberId);
        if (itemListDtos == null || itemListDtos.isEmpty()) return 0;

        // 각 상품 DTO 안의 수량(itemSize)을 누적 합산
        return itemListDtos.stream()
                .mapToInt(ItemListDto::getItemSize)
                .sum(); //
    }

    // 인증(시큐리티) 메서드
    private Long extractMemberId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        if (authentication.getPrincipal() instanceof MyUserDetails userDetails) {
            return userDetails.getMemberEntity().getId();
        }
        return null;
    }

}
