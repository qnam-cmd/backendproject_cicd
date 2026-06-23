package org.spring.backendprojectex.shop.service;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.dto.ItemListDto;

import java.util.List;

public interface CartService {
    // 1. 장바구니 담기
    void insertCart(Long memberId, Long id);
    // 2. 장바구니 담기
    void insertCart2(ItemDto itemDto);
    // 상품 목록
    List<ItemListDto> cartList(Long memberId);
    // 장바구니 갯수
    int countCartItems(Long memberId);
    // 장바구니 비우기
    void clearCart(Long cartId);
}
