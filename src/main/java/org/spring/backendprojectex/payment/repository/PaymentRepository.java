package org.spring.backendprojectex.payment.repository;

import org.spring.backendprojectex.payment.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
    Page<PaymentEntity> findAllByMemberEntityIdOrderByIdDesc(Long memberId, Pageable pageable);

    Page<PaymentEntity> findByMemberEntityId(Long searchMemberId, Pageable pageable);

    Page<PaymentEntity> findByOrderMethodContaining(String search, Pageable pageable);

    Page<PaymentEntity> findByPayResultContaining(String search, Pageable pageable);

    Page<PaymentEntity> findByOrderAddrContaining(String search, Pageable pageable);

    Page<PaymentEntity> findByOrderPostContaining(String search, Pageable pageable);

    Page<PaymentEntity> findByPaymentTypeContaining(String search, Pageable pageable);

    Page<PaymentEntity> findByMemberEntityIdAndPaymentTypeContaining(Long memberId, String search, Pageable pageable);

    Page<PaymentEntity> findByMemberEntityIdAndOrderPostContaining(Long memberId, String search, Pageable pageable);

    Page<PaymentEntity> findByMemberEntityIdAndOrderAddrContaining(Long memberId, String search, Pageable pageable);

    Page<PaymentEntity> findByMemberEntityIdAndPayResultContaining(Long memberId, String search, Pageable pageable);

    Page<PaymentEntity> findByMemberEntityIdAndOrderMethodContaining(Long memberId, String search, Pageable pageable);
}
