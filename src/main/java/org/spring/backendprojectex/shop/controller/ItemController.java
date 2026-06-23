package org.spring.backendprojectex.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.common.PagingUtil;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.service.ItemService;
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

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //============================      상품 등록        ================================//

                // adminController

    //============================      상품 (상세)조회        ================================//

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        ItemDto item = itemService.itemDetailFn(id);
        model.addAttribute("item", item);
        return "shop/itemDetail";
    }
    //============================      상품 (전체)조회        ================================//

//    @GetMapping({"/index", "/", "itemList"})
//    public String itemList(Model model) {
//        List<ItemDto> itemList = itemService.itemListFn();
//        model.addAttribute("itemList", itemList);
//        return "shop/itemList";
//    }

    // Paging
    @GetMapping({"/index", "/", "itemList"})
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
        return "shop/itemList";
    }
}
