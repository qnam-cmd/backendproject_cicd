package org.spring.backendprojectex.community.repository;

import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {
    // 검색어 사용
    Page<CommunityEntity> findByTitleContaining(String search, Pageable pageable);

    Page<CommunityEntity> findByContentContaining(String search, Pageable pageable);

    Page<CommunityEntity> findByNickNameContaining(String search, Pageable pageable);

    // 카테고리, 검색어 사용
    Page<CommunityEntity> findByCategory(String category, Pageable pageable);

    Page<CommunityEntity> findByCategoryAndTitleContaining(String category, String search, Pageable pageable);

    Page<CommunityEntity> findByCategoryAndContentContaining(String category, String search, Pageable pageable);

    Page<CommunityEntity> findByCategoryAndNickNameContaining(String category, String search, Pageable pageable);

    // 조회수증가 쿼리문
    @Modifying
    @Query("update CommunityEntity c set c.count = c.count + 1 where c.id = :id")
    void updateCount(Long id);
}
