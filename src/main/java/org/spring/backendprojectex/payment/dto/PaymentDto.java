package org.spring.backendprojectex.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.payment.entity.PaymentItemEntity;
import org.spring.backendprojectex.shop.entity.CartEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    @Schema(description = "결제 고유 ID", example = "1")
    private Long id;

    //결제방법
    @Schema(description = "결제방법", example = "CARD")
    private String paymentType;

    //주문처
    @Schema(description = "주문처", example = "온라인 쇼핑몰")
    private String orderPost;

    //배송주소
    @Schema(description = "배송주소", example = "서울시 노원구 상계동")
    private String orderAddr;

    //주문금액
    @Schema(description = "결제금액", example = "10000")
    private String payResult;

    //주문방법
    @Schema(description = "주문방법", example = "배달")
    private String orderMethod;

    @Schema(description = "회원 ID", example = "5")
    private Long memberId;

    @Schema(description = "장바구니 ID", example = "12")
    private Long CartId;

    @Schema(description = "회원 엔티티", hidden = true)
    private MemberEntity memberEntity;      // memberEntity.id

    @Schema(description = "장바구니 목록", hidden = true)
    private List<CartEntity> cartEntities;  // cartEntities.get(0).id

    @Schema(description = "결제에 포함된 아이템 목록")
    private List<PaymentItemEntity> paymentItemEntities;    // paymentItemEntities.get(0).id
//    private List<PaymentItemDto> paymentItemDtos;    // paymentItemEntities.get(0).id

    @Schema(description = "생성시간", example = "2026-06-17T13:40:00")
    private LocalDateTime createTime;

    @Schema(description = "수정시간", example = "2026-06-17T13:40:00")
    private LocalDateTime updateTime;
}
