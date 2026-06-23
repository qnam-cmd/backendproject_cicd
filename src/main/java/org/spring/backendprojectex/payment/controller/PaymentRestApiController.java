package org.spring.backendprojectex.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestApiController {

    private final PaymentService paymentService;

    // 2. 비동기 결제 처리 (AJAX)
    @PostMapping("/insert")
    @ResponseBody
    public ResponseEntity<String> insert(@RequestBody PaymentDto paymentDto,
                                         @AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (myUserDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        try {
            // 결제 요청자 강제 세팅 (보안)
            paymentDto.setMemberId(myUserDetails.getMemberEntity().getId());

            // 결제 서비스 실행
            paymentService.insertPayment(paymentDto);

            // 성공 시 HTTP 200 OK와 함께 "1" 이라는 데이터(문자열)를 프론트로 전달
            return ResponseEntity.ok("1");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 처리 중 오류 발생: " + e.getMessage());
        }
    }



}
