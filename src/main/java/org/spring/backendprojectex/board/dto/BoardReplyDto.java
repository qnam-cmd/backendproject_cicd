package org.spring.backendprojectex.board.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.backendprojectex.board.entity.BoardEntity;
import org.spring.backendprojectex.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardReplyDto {

    private Long id;

    @NotBlank(message = "댓글을 작성해주세요")
    private String boardReplyContent;

    @NotBlank(message = "작성자를 입력해주세요")
    private String boardReplyWriter; // 작성자

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String boardReplyPw;    // 댓글 비밀번호

    private Long boardId;

    private Long memberId;

    private BoardEntity boardEntity;

    private MemberEntity memberEntity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
