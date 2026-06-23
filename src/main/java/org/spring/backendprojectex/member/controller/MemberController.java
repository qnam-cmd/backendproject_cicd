package org.spring.backendprojectex.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //============================      회원 가입        ================================//
    // 회원가입
    @GetMapping("/join")
    public String join(MemberDto memberDto) {
        return "member/join";
    }
    // 회원가입 실행
    @PostMapping("/join")
    public String joinOk(@Valid MemberDto memberDto,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "member/join";
        }
        // 회원가입 실행
        memberService.memberInsert(memberDto);
        return "redirect:/member/login";
    }
    //============================      회원 로그인        ================================//
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }


    //============================      회원 수정        ================================//
    //============================      회원 삭제        ================================//
    //============================      회원 (전체)조회        ================================//
    //============================      회원 (상세)조회        ================================//

}
