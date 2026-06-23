package org.spring.backendprojectex.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.common.PagingUtil;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.spring.backendprojectex.open.weather.service.WeatherService;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.shop.service.ItemService;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final WeatherService weatherService;
    private final PaymentService paymentService;

    //============================      회원 목록        ================================//

    @GetMapping({"","/index","/","/member","/admin"})
    public String member(@PageableDefault(page = 0, size=8, sort ="id", direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam(name = "subject", required = false) String subject,
                         @RequestParam(name = "search", required = false) String search,
                         Model model) {
        Page<MemberDto> memberList = memberService.pagingSearchMemberList(pageable, subject, search);
        // common폴더 안 PagingUtil 클래스
        PagingUtil.setPaging(model, memberList, 3);

        model.addAttribute("memberList", memberList);
        model.addAttribute("key", "member");
        return "admin/admin";
    }


    //============================      상품 등록        ================================//

    @GetMapping("/itemInsert")
    private String itemInsert(Model model) {
        model.addAttribute("itemDto", new ItemDto());
        model.addAttribute("key","itemInsert");
        return "admin/admin";
    }
    @PostMapping("/itemInsert")
    public String insertOk(@Valid ItemDto itemDto,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal MyUserDetails myUserDetails,
                           Model model) throws IOException {
        // 유효성검사
        if (bindingResult.hasErrors()) {
            model.addAttribute("key","itemInsert");
            return "admin/admin";
        }
        // 시큐리티에서 회원id가져오기
        itemDto.setMemberId(myUserDetails.getId());
        // db저장
        itemService.insertItem(itemDto);
        // 상품등록 -> 상품목록으로 이동
        return "redirect:/admin/itemList";
    }

    //============================      상품 (전체)조회        ================================//
    @GetMapping("/itemList")
    public String pagingList(@PageableDefault(page = 0, size = 8, sort = "id",
                                     direction = Sort.Direction.DESC) Pageable pageable,
                             @RequestParam(name = "subject", required = false) String subject,
                             @RequestParam(name = "search", required = false) String search,
                             Model model
    ) {
        Page<ItemDto> itemList = itemService.pagingSearchItemFn(pageable,subject,search);

        // common폴더 PagingUtil클래스
        PagingUtil.setPaging(model, itemList, 3);

        model.addAttribute("itemList", itemList);
        model.addAttribute("key","itemList");
        return "admin/admin";
    }

    //============================      상품 (상세)조회        ================================//

    @GetMapping("/item/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        ItemDto item = itemService.itemDetailFn(id);
        model.addAttribute("item", item);
        model.addAttribute("key","itemDetail");
        return "admin/admin";
    }



    //================================      OPEN API      ==================================//
    @Value("${kakao.map.appkey}")
    private String kakaoMapKey;

    @GetMapping("/weather")
    public String weather(Model model) {
        model.addAttribute("kakaoMapKey",kakaoMapKey);
        model.addAttribute("key","weather");
        return "admin/admin";
    }

    @GetMapping("/movie")
    public String movie(Model model) {
        model.addAttribute("kakaoMapKey",kakaoMapKey);
        model.addAttribute("key","movie");
        return "admin/admin";
    }

    @GetMapping("/bus")
    public String bus(Model model) {
        model.addAttribute("kakaoMapKey",kakaoMapKey);
        model.addAttribute("key","bus");
        return "admin/admin";
    }


    //================================      Web Socket      ==================================//

    @GetMapping("/webSocket")
    public String websocket(Model model) {
        model.addAttribute("kakaoMapKey", kakaoMapKey);
        model.addAttribute("key","webSocket");
        return "admin/admin";
    }

    //================================      Payment      ==================================//
    @GetMapping("/payment")
    public String payment(@PageableDefault(page = 0, size = 8, sort = "id",
                                     direction = Sort.Direction.DESC) Pageable pageable,
                             @RequestParam(name = "subject", required = false) String subject,
                             @RequestParam(name = "search", required = false) String search,
                             Model model) {
        Page<PaymentDto> paymentAllPage = paymentService.paymentAllList(pageable,subject,search);
        // common폴더 PagingUtil클래스
        PagingUtil.setPaging(model, paymentAllPage, 3);

        model.addAttribute("paymentList2", paymentAllPage);
        model.addAttribute("key", "payment");

        return "admin/admin";
    }
}
