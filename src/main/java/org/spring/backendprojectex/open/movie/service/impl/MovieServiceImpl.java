package org.spring.backendprojectex.open.movie.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.open.movie.dto.MovieDto;
import org.spring.backendprojectex.open.movie.dto.MovieItem;
import org.spring.backendprojectex.open.movie.dto.MovieListResponse;
import org.spring.backendprojectex.open.movie.entity.MovieEntity;
import org.spring.backendprojectex.open.movie.repository.MovieRepository;
import org.spring.backendprojectex.open.movie.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<MovieDto> insertResponseBody(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieListResponse movieListResponse = null;
        try {
            // JSON 문자열을 Java 객체로 변환
            movieListResponse = objectMapper.readValue(responseBody, MovieListResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(movieListResponse == null || movieListResponse.getMovieListResult()== null){
            return null;
        }
        List<MovieItem> movieItems = movieListResponse.getMovieListResult().getMovieList();
        AtomicReference<String> companyCd = new AtomicReference<>("");
        AtomicReference<String> companyNm = new AtomicReference<>("");
        for (MovieItem movieItem : movieItems) {
            System.out.println("디버깅 - movieCd 값: " + movieItem.getMovieCd()); // 값이 제대로 찍히는지 확인!
            // 회사정보가 여러개일 경우 마지막 값을 사용
            movieItem.getCompanys().forEach(company -> {
                companyCd.set(company.getCompanyCd());
                companyNm.set(company.getCompanyNm());
            });
            Optional<MovieEntity> movieEntity1 = movieRepository.findByMovieCd(movieItem.getMovieCd());
            if(!movieEntity1.isPresent()) {
                System.out.println("DEBUG: movieItem의 영화 제목 확인 -> " + movieItem.getMovieNm());
                MovieEntity movieEntity = MovieEntity.builder()
                        .movieCd(movieItem.getMovieCd())
                        .movieNm(movieItem.getMovieNm())
                        .movieNmEn(movieItem.getMovieNmEn())
                        .prdtYear(movieItem.getPrdtYear())
                        .repNationNm(movieItem.getRepNationNm())
                        .typeNm(movieItem.getTypeNm())
                        .openDt(movieItem.getOpenDt())
                        .genreAlt(movieItem.getGenreAlt())
                        .prdtStatNm(movieItem.getPrdtStatNm())
                        .companyCd(companyCd.get())
                        .companyNm(companyNm.get())
                        .source("영화진흥위원회(KOBIS)")
                        .build();
                System.out.println("데이터 저장 시도 중...");
                movieRepository.save(movieEntity);
            }
        }
        List<MovieEntity> movieEntities = movieRepository.findAll();
        if(movieEntities.isEmpty()) {
            throw new NullPointerException("목록이 없습니다.");
        }
        String totCnt = movieListResponse.getMovieListResult().getTotCnt();
        String source = movieListResponse.getMovieListResult().getSource();
        return movieEntities.stream().map(el->
                MovieDto.builder()
                        .id(el.getId())
                        .movieCd(el.getMovieCd())
                        .movieNm(el.getMovieNm())
                        .movieNmEn(el.getMovieNmEn())
                        .prdtYear(el.getPrdtYear())
                        .repGenreNm(el.getRepGenreNm())
                        .repNationNm(el.getRepNationNm())
                        .typeNm(el.getTypeNm())
                        .openDt(el.getOpenDt())
                        .genreAlt(el.getGenreAlt())
                        .prdtStatNm(el.getPrdtStatNm())
                        .companyCd(companyCd.get())
                        .companyNm(companyNm.get())
                        .source(source) //영화사이름
                        .totCnt(totCnt) // 총제작편수
                .build()).toList();

    }

    @Override
    public MovieDto movieInfoJava(String movieCd) {
        Optional<MovieEntity> movieEntity1 =
                movieRepository.findByMovieCd(movieCd);
        MovieEntity movieItem=movieEntity1.get();

        System.out.println(movieItem+" << movieItem");
        if (movieEntity1.isPresent()) {
            return  MovieDto.builder()
                    .id(movieItem.getId())
                    .movieCd(movieItem.getMovieCd())
                    .movieNm(movieItem.getMovieNm())
                    .movieNmEn(movieItem.getMovieNmEn())
                    .prdtYear(movieItem.getPrdtYear())
                    .repNationNm(movieItem.getRepNationNm())
                    .typeNm(movieItem.getTypeNm())
                    .openDt(movieItem.getOpenDt())
                    .genreAlt(movieItem.getGenreAlt())
                    .prdtStatNm(movieItem.getPrdtStatNm())
                    .build();
        }
        return null;
    }

    @Override
    public Page<MovieDto> getMovieList(Pageable pageable, String subject, String search) {
        Page<MovieEntity>  moviePage=null;

        System.out.println(subject+" <<sub");
        System.out.println(search+" <<search");
        if(subject==null || search==null || search.length()<=0 ){
            moviePage=movieRepository.findAll(pageable);
        }else{
            if(subject.equals("movieNm")){
                moviePage=movieRepository.findByMovieNmContaining(pageable,search);
            }else if(subject.equals("typeNm")){
                moviePage=movieRepository.findByTypeNmContaining(pageable,search);
            }else if(subject.equals("repNationNm")){
                moviePage=movieRepository.findByRepNationNmContaining(pageable,search);
            }else if(subject.equals("genreAlt")){
                moviePage=movieRepository.findByGenreAltContaining(pageable,search);
            }else if(subject.equals("prdtStatNm")){ //prdtStatNm
                moviePage=movieRepository.findByPrdtStatNmContaining(pageable,search);
            } else if(subject.equals("openDt")) {
                moviePage = movieRepository.findByOpenDtContaining(pageable, search);
            }
            else{
                moviePage=movieRepository.findAll(pageable);
            }
        }

        return moviePage.map(el -> MovieDto.builder()
                .id(el.getId())
                .movieCd(el.getMovieCd())
                .movieNm(el.getMovieNm())
                .movieNmEn(el.getMovieNmEn())
                .prdtYear(el.getPrdtYear())
                .repGenreNm(el.getRepGenreNm())
                .repNationNm(el.getRepNationNm())
                .typeNm(el.getTypeNm())
                .openDt(el.getOpenDt())
                .genreAlt(el.getGenreAlt())
                .prdtStatNm(el.getPrdtStatNm())
                .companyCd(el.getCompanyCd())
                .companyNm(el.getCompanyNm())
                .source(el.getSource())  // 필요시 필드 추가
                .totCnt(el.getTotCnt())  // 필요시 필드 추가
                .build());
    }

}
