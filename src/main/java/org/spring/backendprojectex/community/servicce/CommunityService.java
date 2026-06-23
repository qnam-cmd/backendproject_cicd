package org.spring.backendprojectex.community.servicce;

import org.spring.backendprojectex.community.dto.CommunityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CommunityService {
    // 게시글 목록 (검색,페이징)
    Page<CommunityDto> communityList(Pageable pageable, String category , String subject, String search);
    // 게시글 상세
    CommunityDto communityDetail(Long id);
    // 게시글 등록
    void communityInsert(CommunityDto communityDto) throws IOException;
    // 게시글 삭제
    void deleteCommunity(Long id, Long memberId);
    // 조회수
    void count(Long id);
}
