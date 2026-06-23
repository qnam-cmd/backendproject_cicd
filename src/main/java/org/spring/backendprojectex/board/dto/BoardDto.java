package org.spring.backendprojectex.board.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    private String boardTitle;       // 글 제목

    @NotBlank(message = "내용을 입력해주세요")
    private String boardContent;     // 글 내용

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String boardPw;     // 글비밀번호

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;    //닉네임

    private int hit;            //조회수

    private MemberEntity memberEntity;

    @NotNull
    private Long memberId;  // 로그인한 아이디

    private int attachFile;    // 파일첨부 유무

    private MultipartFile boardFile;    // 실제파일(이미지)

    private String newFileName; // 새이름 -> DB, 로컬 저장소 저장이름

    private String oldFileName; // 파일 원래이름

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
