package org.spring.backendprojectex.open.movie.service;

import org.spring.backendprojectex.open.movie.dto.boxOffice.BoxOfficeDto;

import java.util.List;

public interface BoxOfficeService {
    List<BoxOfficeDto> insertBoxOfficeBody(String boxOfficeBody);
    BoxOfficeDto boxOfficeMovieInfoJava(String movieCd);
}
