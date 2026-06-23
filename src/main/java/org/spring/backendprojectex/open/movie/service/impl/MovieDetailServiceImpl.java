package org.spring.backendprojectex.open.movie.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.backendprojectex.open.movie.dto.movie.MovieDetailDto;
import org.spring.backendprojectex.open.movie.dto.movie.MovieInfo;
import org.spring.backendprojectex.open.movie.dto.movie.MovieInfoResultReson;
import org.spring.backendprojectex.open.movie.entity.MovieDetailEntity;
import org.spring.backendprojectex.open.movie.repository.MovieDetailRepository;
import org.spring.backendprojectex.open.movie.repository.MovieRepository;
import org.spring.backendprojectex.open.movie.service.MovieDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class MovieDetailServiceImpl implements MovieDetailService {

    private final MovieDetailRepository movieDetailRepository;
    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void movieInfoResultFn(String responseBody) {
        try {
            // JSON -> Java 객체 매핑
            MovieInfoResultReson result = objectMapper.readValue(responseBody, MovieInfoResultReson.class);

            // 영화 정보 추출
            // var 타입 추론
//            var movieInfo = result.getMovieInfoResult().getMovieInfo();
            MovieInfo movieInfo = result.getMovieInfoResult().getMovieInfo();
            String movieNm = movieInfo.getMovieNm();

            // 영화명이 이미 존재하는지 확인
            Optional<MovieDetailEntity> optionalMovieDetailEntity = movieDetailRepository.findByMovieCd(movieInfo.getMovieCd());


            // 1. 감독과 배우 안전하게 추출 (Null 방지)
            String directorStr = (movieInfo.getDirectors() != null && !movieInfo.getDirectors().isEmpty())
                    ? movieInfo.getDirectors().get(0).getPeopleNm() : "정보없음";

            String actorsStr = (movieInfo.getActors() != null && !movieInfo.getActors().isEmpty())
                    ? movieInfo.getActors().stream().map(a -> a.getPeopleNm()).collect(Collectors.joining(", ")) : "정보없음";

//            if (optionalMovieDetailEntity.isEmpty()) {
            if (!optionalMovieDetailEntity.isPresent()) {
                // 새 영화 정보 저장
                MovieDetailEntity newMovie = MovieDetailEntity.builder()
                        .movieCd(movieInfo.getMovieCd())
                        .movieNm(movieInfo.getMovieNm())
                        .movieNmEn(movieInfo.getMovieNmEn())
                        .openDt(movieInfo.getOpenDt())
                        .prdtStatNm(movieInfo.getPrdtStatNm())
                        .prdtYear(movieInfo.getPrdtYear())
                        .showTm(movieInfo.getShowTm())
                        .typeNm(movieInfo.getTypeNm())
                        .source(result.getMovieInfoResult().getSource())
                        .director(directorStr)
                        .actors(actorsStr)
                        .build();

                movieDetailRepository.save(newMovie);
            } else {
                log.info("영화 '{}' 는 이미 DB에 존재합니다.", movieNm);
            }

        } catch (Exception e) {
            e.printStackTrace(); //
            log.error("영화 정보 파싱 또는 저장 중 오류 발생", e);
        }
    }

    @Override
    public MovieDetailDto movieInfoJava(String movieCd) {

        System.out.println(movieCd+" movieId");
        Optional<MovieDetailEntity> movieInfo = movieDetailRepository.findByMovieCd(movieCd);

        if(movieInfo.isPresent()){
            return MovieDetailDto
                    .builder()
                    .id(movieInfo.get().getId())
                    .movieCd(movieInfo.get().getMovieCd())
                    .movieNm(movieInfo.get().getMovieNm())
                    .openDt(movieInfo.get().getOpenDt())
                    .prdtStatNm(movieInfo.get().getPrdtStatNm())
                    .showTm(movieInfo.get().getShowTm())
                    .source(movieInfo.get().getSource())
                    .actors(movieInfo.get().getActors())
                    .director(movieInfo.get().getDirector())
//                    .movieNmEn(movieInfo.get().getMovieNmEn())
//                    .prdtYear(movieInfo.get().getPrdtYear())
//                    .typeNm(movieInfo.get().getTypeNm())
                    .build();
        }
        return null;
    }
}
