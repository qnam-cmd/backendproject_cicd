package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByMemberEntityId(Long id);

    int countByMemberEntity(MemberEntity build);
}
