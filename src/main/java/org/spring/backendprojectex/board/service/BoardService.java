package org.spring.backendprojectex.board.service;

import org.spring.backendprojectex.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

public interface BoardService {
    // 게시글 작성
    void boardInsert(BoardDto boardDto) throws IOException;

    // 게시글 상세조회
    BoardDto boardDetail(Long id);

    // 게시글 삭제
    void boardDelete(Long id);

    // 게시글 수정 (파일존재)
    void boardUpdate(BoardDto boardDto) throws IOException;

    // 조회수 증가
    void updateHit(Long id);

    // 게시글 검색
    Page<BoardDto> boardList(Pageable pageable, String subject, String search);

}
