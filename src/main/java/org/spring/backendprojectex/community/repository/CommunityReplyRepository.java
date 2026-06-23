package org.spring.backendprojectex.community.repository;

import org.spring.backendprojectex.community.entity.CommunityReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityReplyRepository extends JpaRepository<CommunityReplyEntity, Long> {
    List<CommunityReplyEntity> findByCommunityEntity_Id(Long communityId);
}
