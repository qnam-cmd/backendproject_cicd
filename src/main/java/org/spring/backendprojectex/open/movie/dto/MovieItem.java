package org.spring.backendprojectex.open.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieItem {

    private String movieCd;
    private String movieNm;     // 영화명(국문)
    private String movieNmEn;   // 전일대비 순위 증감분
    private String prdtYear;    // 랭킹에 신규진입여부"OLD" : 기존,
    private String openDt;      // 개봉일
    private String nationAlt;
    private String prdtStatNm;
    private String repGenreNm;
    private String repNationNm;
    private String typeNm;
    private String genreAlt;            //
    private List<Director> directors;  // 실제 영화 리스트
    private List<Company> companys;     // 실제영화 리스트
}
