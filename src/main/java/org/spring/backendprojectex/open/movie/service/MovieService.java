package org.spring.backendprojectex.open.movie.service;


import org.spring.backendprojectex.open.movie.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    public List<MovieDto> insertResponseBody(String responseBody);
    MovieDto movieInfoJava(String movieCd);
    Page<MovieDto> getMovieList(Pageable pageable, String subject, String search);
}
