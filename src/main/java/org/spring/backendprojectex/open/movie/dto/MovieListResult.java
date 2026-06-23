package org.spring.backendprojectex.open.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieListResult {
    private String source;  // 영화 소스제공
    private String totCnt;  // 전체 영화수
    private List<MovieItem> movieList; // 실제영화 목록
}
