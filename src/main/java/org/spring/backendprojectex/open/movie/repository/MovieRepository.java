package org.spring.backendprojectex.open.movie.repository;


import org.spring.backendprojectex.open.movie.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByMovieCd(String movieCd);

    Page<MovieEntity> findByMovieNmContaining(Pageable pageable, String search);

    Page<MovieEntity> findByTypeNmContaining(Pageable pageable, String search);

    Page<MovieEntity> findByRepNationNmContaining(Pageable pageable, String search);

    Page<MovieEntity> findByGenreAltContaining(Pageable pageable, String search);

    Page<MovieEntity> findByPrdtStatNmContaining(Pageable pageable, String search);

    Page<MovieEntity> findByOpenDtContaining(Pageable pageable, String search);
}
