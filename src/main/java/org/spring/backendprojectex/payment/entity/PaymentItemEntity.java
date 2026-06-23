package org.spring.backendprojectex.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "payment_item_tb06")
public class PaymentItemEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_item_id")
    private Long id;

    private String paymentItemTitle;    // 결제 상품 이름

    private int paymentItemPrice;       // 결제 상품 가격

    private int paymentItemSize;        // 결제 상품 크기(갯수)

    // N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;
}
