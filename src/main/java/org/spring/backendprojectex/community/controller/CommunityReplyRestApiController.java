package org.spring.backendprojectex.community.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.community.dto.CommunityReplyDto;
import org.spring.backendprojectex.community.servicce.CommunityReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communityReply")
public class CommunityReplyRestApiController {

    private final CommunityReplyService communityReplyService;

    // 댓글 작성
    @PostMapping("/save")
    public ResponseEntity<String> saveReply(
            @RequestBody CommunityReplyDto dto) {
        communityReplyService.insertReply(dto);
        return ResponseEntity.ok("댓글 등록 성공");
    }

    // 게시글 댓글 목록
    @GetMapping("/list/{communityId}")
    public ResponseEntity<List<CommunityReplyDto>> replyList(@PathVariable Long communityId) {
        List<CommunityReplyDto> replyList =
                communityReplyService.communityReplyList(communityId);
        return ResponseEntity.ok(replyList);
    }

    // 댓글 상세
    @GetMapping("/{replyId}")
    public ResponseEntity<CommunityReplyDto> detail(@PathVariable Long replyId) {
        CommunityReplyDto dto = communityReplyService.communityReplyDetail(replyId);
        return ResponseEntity.ok(dto);
    }

    // 댓글 수정
    @PutMapping("/update/{replyId}")
    public ResponseEntity<String> updateReply(@PathVariable Long replyId,
                                              @RequestParam String replyContent) {
        communityReplyService.updateReply(
                replyId,
                replyContent
        );
        return ResponseEntity.ok("댓글 수정 성공");
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable Long replyId) {
        communityReplyService.deleteReply(replyId);
        return ResponseEntity.ok("댓글 삭제 성공");
    }
}