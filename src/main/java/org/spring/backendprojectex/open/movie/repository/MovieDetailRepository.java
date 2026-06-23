package org.spring.backendprojectex.open.movie.repository;


import org.spring.backendprojectex.open.movie.entity.MovieDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieDetailRepository extends JpaRepository<MovieDetailEntity,Long> {
  Optional<MovieDetailEntity> findByMovieNm(String movieNm);

    Optional<MovieDetailEntity> findByMovieCd(String movieCd);
}
