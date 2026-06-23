package org.spring.backendprojectex.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.config.MyUserDetails;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.dto.ItemReplyDto;
import org.spring.backendprojectex.shop.service.ItemReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/itemReply")
public class ItemReplyRestApiController {
    private final ItemReplyService itemReplyService;

    // ========================      상품 댓글 작성      ===============================//
    @PostMapping("/insert")
    public ResponseEntity<String> save(
            @Valid @RequestBody ItemReplyDto itemReplyDto,
                                       @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        // 1. 비로그인 사용자 방어 (세션이 만료되었거나 로그아웃 상태일 때)
        if (myUserDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
        }
        // 2. 시큐리티 회원 ID주입
        MemberEntity member = MemberEntity.builder()
                .id(myUserDetails.getId())
                .build();
        itemReplyDto.setMemberEntity(member);
        // 3. 저장
        itemReplyService.insertItemReply(itemReplyDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("상품댓글 작성 성공");
    }

    // ========================      상품 댓글 (전체)조회      ===============================//
    @GetMapping("/list/{itemId}")
    public ResponseEntity<List<ItemReplyDto>> list(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemReplyService.itemReplyList(itemId));
    }

    // ========================      상품 댓글 (상세)조회      ===============================//
    @GetMapping("/detail/{id}")
    public ResponseEntity<ItemReplyDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(itemReplyService.itemReplyDetail(id));
    }

//    // ========================      상품 댓글 삭제      ===============================//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> delete(@PathVariable Long id,
//                                         @AuthenticationPrincipal MyUserDetails myUserDetails
//    ) {
//        // 시큐리티 회원ID 확인
//        if (myUserDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//        // 상품댓글id와 시큐리티회원id 같이 넘기기
//        itemReplyService.deleteItemReply(id, myUserDetails.getId());
//        return ResponseEntity.ok("댓글삭제 성공");
//    }
//
//    // ========================      상품 댓글 수정      ===============================//
//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestBody ItemReplyDto itemReplyDto,
//                                         @AuthenticationPrincipal MyUserDetails myUserDetails
//    ) {
//        // 시큐리티 회원ID 확인
//        if (myUserDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//        // 상품댓글id와 시큐리티회원id 같이 넘기기
//        itemReplyService.updateItemReply(itemReplyDto, myUserDetails.getId());
//        return ResponseEntity.ok("댓글수정 성공");
//    }
}
