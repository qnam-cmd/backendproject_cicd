package org.spring.backendprojectex.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.payment.entity.PaymentEntity;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_tb06")
public class CartEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // 1:1-> 하나의 Entity만 작성
    @OneToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    // 1:N
    @OneToMany(mappedBy = "cartEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<ItemListEntity> itemListEntities;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;    // 상품을 결제에 연결
}
