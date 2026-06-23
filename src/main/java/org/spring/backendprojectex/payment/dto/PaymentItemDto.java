package org.spring.backendprojectex.payment.dto;

import lombok.*;
import org.spring.backendprojectex.payment.entity.PaymentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItemDto {
    private Long id;

    private String paymentItemTitle;    // 결제 상품 이름

    private int paymentItemPrice;       // 결제 상품 가격

    private int paymentItemSize;        // 결제 상품 크기(갯수)

    private PaymentEntity paymentEntity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
