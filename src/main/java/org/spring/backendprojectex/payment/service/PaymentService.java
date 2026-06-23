package org.spring.backendprojectex.payment.service;

import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    // 결제내역 생성
    void insertPayment(PaymentDto paymentDto);
    // 특정ID 결제내역(전체)조회
    Page<PaymentDto> paymentList(Pageable pageable, String subject, String search, Long memberId);
    // 특정ID 결제내역(상세)조회
    PaymentDto findPaymentById(Long id);
    // 결제내역 (전체)조회
    Page<PaymentDto> paymentAllList(Pageable pageable, String subject, String search);
}
