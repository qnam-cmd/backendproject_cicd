package org.spring.backendprojectex.member.repository;

import jakarta.validation.constraints.NotBlank;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUserEmail(@NotBlank(message = "이메일을 입력하세요") String userEmail);

    Page<MemberEntity> findByUserEmailContaining(Pageable pageable, String search);

    Page<MemberEntity> findByUserNameContaining(Pageable pageable, String search);
}
