package org.spring.backendprojectex.shop.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.shop.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 본인 장바구니 목록 조회 화면
    // id 파라미터값으로 조회
    @GetMapping("/cartList/{memberId}")
    public String cartList(@PathVariable("memberId") Long memberId, Model model) {
        List<ItemListDto> itemListDtos = cartService.cartList(memberId);
        model.addAttribute("itemList", itemListDtos);
        model.addAttribute("memberId", memberId);
        return "shop/cartList";
    }

    // id파라미터없이 로그인세션으로 장바구니 조회
    @GetMapping("/cart")
    public String cartList(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {

        // 1. 비로그인 접근 차단
        if (myUserDetails == null) {
            return "redirect:/member/login";
        }

        // 2. 세션에서 내 ID를 직접 꺼냄
        Long memberId = myUserDetails.getMemberEntity().getId();

        // 3. 내 장바구니 목록 조회
        List<ItemListDto> itemListDtos = cartService.cartList(memberId);

        model.addAttribute("itemList", itemListDtos);
        return "shop/cartList";
    }


    // 1. 장바구니 담기   ( 기본 Form 전송 방식)
    @PostMapping("/insert/addCart0")
    public String addCart0(@ModelAttribute ItemDto itemDto,
                           @AuthenticationPrincipal MyUserDetails myUserDetails) {

        // 로그인세션 id로 주입
        itemDto.setMemberId(myUserDetails.getMemberEntity().getId());
        cartService.insertCart2(itemDto);
        return "redirect:/shop/cart";
    }

    // 2. 장바구니 담기 (비동기 AJAX/JSON 요청방식)
    @PostMapping("/insert/addCart2")
    @ResponseBody
    public String addCart2(@RequestBody ItemDto itemDto,
                           @AuthenticationPrincipal MyUserDetails myUserDetails) {

        // 로그인세션 id로 주입
        itemDto.setMemberId(myUserDetails.getMemberEntity().getId());
        cartService.insertCart2(itemDto);
        return "1"; // 성공시 1
    }

    // 3. 장바구니 담기 (URL 경로 파라미터 방식 - 수량 1개 고정)
    @GetMapping("/insert/memberId/{memberId}/id/{id}")
    public String addCart1(@PathVariable("memberId") Long memberId,
                           @PathVariable("id") Long id) {
        cartService.insertCart(memberId, id);
        return "redirect:/shop/cart";
    }

}
