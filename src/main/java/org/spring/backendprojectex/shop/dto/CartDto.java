package org.spring.backendprojectex.shop.dto;

import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.spring.backendprojectex.payment.entity.PaymentEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private Long id;

    private Long memberId;

    private MemberEntity memberEntity;

    private List<ItemListEntity> itemListEntities;

    private PaymentEntity paymentEntity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
