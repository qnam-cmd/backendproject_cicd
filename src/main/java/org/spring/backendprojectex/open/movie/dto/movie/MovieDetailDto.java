package org.spring.backendprojectex.open.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDetailDto {

  private Long id;

  private String movieCd; // 영화코드
  private String movieNm; // 영화이름
  private String movieNmEn;// 영화명(영어)
  private String openDt;// 개봉일
  private String prdtStatNm;// 개봉유무
  private String prdtYear;// 제작년도
  private String showTm;// 상영시간
  private String typeNm;// 유형
  private String source;// 주관
  private String director;// 주관
  private String actors;// 주관

}
