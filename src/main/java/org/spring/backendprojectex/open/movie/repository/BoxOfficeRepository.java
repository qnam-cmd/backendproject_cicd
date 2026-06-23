package org.spring.backendprojectex.open.movie.repository;

import org.spring.backendprojectex.open.movie.entity.BoxOfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxOfficeRepository extends JpaRepository<BoxOfficeEntity,Long> {
    List<BoxOfficeEntity> findByMovieCd(String movieCd);
}
