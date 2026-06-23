package org.spring.backendprojectex.board.dto;

import lombok.*;
import org.spring.backendprojectex.board.entity.BoardEntity;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFileDto {

    private Long id;

    private String newFileName; // 새이름 -> DB, 로컬 저장소 저장이름

    private String oldFileName; // 파일 원래이름

    private Long boardId;   // board_id

    private BoardEntity boardEntity;    // board.getMemberEntity().id

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
