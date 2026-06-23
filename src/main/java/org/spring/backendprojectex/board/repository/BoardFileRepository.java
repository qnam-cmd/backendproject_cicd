package org.spring.backendprojectex.board.repository;

import org.spring.backendprojectex.board.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity,Long> {
    Optional<BoardFileEntity> findByBoardEntity_Id(Long id);

    void deleteByBoardEntity_Id(Long id);
}
