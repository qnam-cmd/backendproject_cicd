package org.spring.backendprojectex.payment.repository;

import org.spring.backendprojectex.payment.entity.PaymentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentItemRepository extends JpaRepository<PaymentItemEntity, Long> {
    Optional<PaymentItemEntity> findByPaymentEntityId(Long id);

    @Query(value = " select i.* from " +
    "payment_tb06 p inner join payment_item_tb06 i" +
            "on p.payment_id= i.payment_id" +
            "where p.member_id=:id",nativeQuery = true)
    List<PaymentItemEntity> findAllNative(@Param("id") Long id);
}
