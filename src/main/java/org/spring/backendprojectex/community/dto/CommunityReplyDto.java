package org.spring.backendprojectex.community.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.spring.backendprojectex.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityReplyDto {

    private Long id;

    @NotBlank(message = "댓글을 작성해주세요")
    private String replyContent;

    @NotBlank(message = "작성자를 입력해주세요")
    private String replyWriter; // 작성자

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String replyPw;    // 댓글 비밀번호

    private Long memberId;
    private Long communityId;

//    private CommunityEntity communityEntity;
//    private MemberEntity memberEntity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
