package org.spring.backendprojectex.board.repository;

import org.spring.backendprojectex.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity,Long> {
    Page<BoardEntity> findByBoardTitleContaining(String search, Pageable pageable);

    Page<BoardEntity> findByBoardContentContaining(String search, Pageable pageable);

    Page<BoardEntity> findByNickNameContaining(String search, Pageable pageable);
}
