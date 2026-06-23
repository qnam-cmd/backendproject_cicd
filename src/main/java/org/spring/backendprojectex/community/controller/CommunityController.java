package org.spring.backendprojectex.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.common.PagingUtil;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.spring.backendprojectex.community.servicce.CommunityService;
import org.spring.backendprojectex.config.MyUserDetails;
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
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;


    //============================      게시글 작성        ================================//

    @GetMapping("/write")
    public String writer() {
        return "community/communityWriter";
    }

    @PostMapping("/write")
    public String writerOk (@Valid CommunityDto communityDto,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        if(bindingResult.hasErrors()) {
            return "community/communityWriter";
        }
        // 시큐리티 로그인세션id 가져오기
        communityDto.setMemberId(myUserDetails.getId());
        // db저장
        communityService.communityInsert(communityDto);

        return "redirect:/community/communityList";
    }

    //============================      게시글 (전체)목록        ================================//

    @GetMapping({"","/communityList"})
    public String communityList(Model model,
                            @PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "category", required = false) String category,
                            @RequestParam(name = "subject", required = false) String subject,
                            @RequestParam(name = "search", required = false) String search) {

        Page<CommunityDto> communityList = communityService.communityList(pageable, category ,subject,search);

        PagingUtil.setPaging(model,communityList,5);

        model.addAttribute("communityList",communityList);

        return "community/communityList";
    }


    //============================      게시글 (상세)목록        ================================//

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        CommunityDto community = communityService.communityDetail(id);
        model.addAttribute("community",community);
        return "community/communityDetail";
    }


}
