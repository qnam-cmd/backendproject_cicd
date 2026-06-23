package org.spring.backendprojectex.board.repository;

import org.spring.backendprojectex.board.entity.BoardReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReplyRepository extends JpaRepository<BoardReplyEntity,Long> {
}
