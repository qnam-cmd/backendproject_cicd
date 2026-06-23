package org.spring.backendprojectex.community.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {

    public Long id;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;

    private String category;    // 공지사항,질문,Q&A,,,

    private int count;  // 조회수

    private int attachFile;    // 파일첨부 유무

    private MultipartFile communityFile;    // 실제파일(이미지)

    private MemberEntity memberEntity;

    private Long memberId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
