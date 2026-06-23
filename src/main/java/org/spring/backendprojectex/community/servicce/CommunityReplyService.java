package org.spring.backendprojectex.community.servicce;
import org.spring.backendprojectex.community.dto.CommunityReplyDto;

import java.util.List;

public interface CommunityReplyService {
    // 게시글 댓글 등록
    void insertReply(CommunityReplyDto communityReplyDto);
    // 게시글 댓글(전체)목록
    List<CommunityReplyDto> communityReplyList(Long communityId);
    // 게시글 댓글(상세)목록
    CommunityReplyDto communityReplyDetail(Long id);
    // 댓글 수정
    void updateReply(Long id, String replyContent);
    // 댓글 삭제
    void deleteReply(Long id);
}
