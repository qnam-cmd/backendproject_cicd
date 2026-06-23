package org.spring.backendprojectex.admin.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Admin", description = "관리자페이지 관련 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminRestApiController {
    private final MemberService memberService;
    private final PaymentService paymentService;

    @GetMapping("/member/detail/{id}")
    public ResponseEntity<?> memberDetail(@PathVariable("id")Long id) {
        Map<String , MemberDto> map = new HashMap<>();
        MemberDto memberDto = memberService.memberDetail(id);
        map.put("member", memberDto);

        log.info("=========memberDto={}============", memberDto);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @GetMapping("/payment/detail/{paymentId}")
    public ResponseEntity<?> paymentDetail(@PathVariable("paymentId") Long paymentId,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (myUserDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        try {
            // 조회
            PaymentDto paymentDto = paymentService.findPaymentById(paymentId);
            // 성공시 JSON데이터 반환
            return ResponseEntity.status(HttpStatus.OK).body(paymentDto);
        }catch (IllegalArgumentException e) {
            // 결제 내역을 찾을 수 없는 경우 400(Bad Request) 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 에러 500 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

}
