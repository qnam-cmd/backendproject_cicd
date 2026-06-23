package org.spring.backendprojectex.community.servicce.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.community.dto.CommunityReplyDto;
import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.spring.backendprojectex.community.entity.CommunityReplyEntity;
import org.spring.backendprojectex.community.repository.CommunityReplyRepository;
import org.spring.backendprojectex.community.repository.CommunityRepository;
import org.spring.backendprojectex.community.servicce.CommunityReplyService;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityReplyServiceImpl implements CommunityReplyService {

    private final CommunityReplyRepository communityReplyRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;


    // ======================================     댓글 작성     ==========================================//

    @Override
    public void insertReply(CommunityReplyDto communityReplyDtoDto) {
        // 게시글확인
        CommunityEntity community = communityRepository.findById(communityReplyDtoDto.getCommunityId())
                .orElseThrow(()-> new IllegalArgumentException("상품(ID)이 존재하지 않습니다."));
        // 회원 확인
        MemberEntity member = memberRepository.findById(communityReplyDtoDto.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("회원(ID)이 존재하지 않습니다."));
        // DTO -> 엔티티 변환
        CommunityReplyEntity communityReplyEntity = CommunityReplyEntity.builder()
                .memberEntity(member)
                .communityEntity(community)
                .replyWriter(communityReplyDtoDto.getReplyWriter())
                .replyContent(communityReplyDtoDto.getReplyContent())
                .replyPw(communityReplyDtoDto.getReplyPw())
                .build();
        communityReplyRepository.save(communityReplyEntity);
    }

    // ======================================     댓글 목록 (게시글(id)의 전체댓글     ==========================================//

    @Override
    public List<CommunityReplyDto> communityReplyList(Long communityId) {
        List<CommunityReplyEntity> communityReplyEntities = communityReplyRepository.findByCommunityEntity_Id(communityId);

        return communityReplyEntities.stream().map(reply ->
                CommunityReplyDto.builder()
                        .id(reply.getId())
                        .createTime(reply.getCreateTime())
                        .updateTime(reply.getUpdateTime())
                        .replyContent(reply.getReplyContent())
                        .replyWriter(reply.getReplyWriter())
                        .replyPw(reply.getReplyPw())
                        .memberId(reply.getMemberEntity().getId())
                        .communityId(reply.getCommunityEntity().getId())
                        .build())
                .toList();
    }

    // ======================================     댓글 목록 (회원(id)또는 게시글(id)) 상세댓글     ==========================================//

    @Override
    public CommunityReplyDto communityReplyDetail(Long id) {

        CommunityReplyEntity reply = communityReplyRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글(ID)이 없습니다."));
        return CommunityReplyDto.builder()
                .id(reply.getId())
                .replyWriter(reply.getReplyWriter())
                .replyContent(reply.getReplyContent())
                .replyPw(reply.getReplyPw())
                .communityId(reply.getCommunityEntity().getId())
                .memberId(reply.getMemberEntity().getId())
                .createTime(reply.getCreateTime())
                .updateTime(reply.getUpdateTime())
                .build();
    }
    // ======================================     댓글 수정     ==========================================//
    @Override
    public void updateReply(Long id, String replyContent) {
        CommunityReplyEntity reply = communityReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        reply.setReplyContent(replyContent);

        communityReplyRepository.save(reply);
    }
    // ======================================     댓글 삭제     ==========================================//

    @Override
    public void deleteReply(Long id) {
        CommunityReplyEntity reply = communityReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        communityReplyRepository.delete(reply);
    }
}
