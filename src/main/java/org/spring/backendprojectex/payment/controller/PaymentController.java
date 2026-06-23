package org.spring.backendprojectex.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.common.PagingUtil;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.shop.service.CartService;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final CartService cartService;
    private final PaymentService paymentService;

    // 1. 주문/결제 작성 페이지 이동
    @GetMapping({"","/"})
    public String index(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {
        if (myUserDetails == null) {
            return "redirect:/member/login";
        }
        Long memberId = myUserDetails.getMemberEntity().getId();

        List<ItemListDto> itemListDtos = cartService.cartList(memberId);
        model.addAttribute("itemList", itemListDtos);
        model.addAttribute("memberId", memberId);
        return "payment/index"; // 결제 페이지 뷰 경로
    }

    // 3. 주문내역 목록 조회 페이지 이동
    @GetMapping("/paymentList")
    public String paymentList(@AuthenticationPrincipal MyUserDetails myUserDetails,
                              @PageableDefault(page = 0, size = 8, sort = "id",
                                      direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(name = "subject", required = false) String subject,
                              @RequestParam(name = "search", required = false) String search,
                              Model model) {
        if (myUserDetails == null) {
            return "redirect:/member/login";
        }
        Long memberId = myUserDetails.getMemberEntity().getId();

        Page<PaymentDto> paymentPage = paymentService.paymentList(pageable,subject,search,memberId);
        // common폴더 PagingUtil클래스
        PagingUtil.setPaging(model, paymentPage, 3);

        model.addAttribute("paymentList",paymentPage);
        return "payment/paymentList";   // 주문내역 뷰 경로
    }



}
