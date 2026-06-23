package org.spring.backendprojectex.open.movie.service;


import org.spring.backendprojectex.open.movie.dto.movie.MovieDetailDto;

public interface MovieDetailService {


    void movieInfoResultFn(String responseBody);

    MovieDetailDto movieInfoJava(String movieCd);
}
