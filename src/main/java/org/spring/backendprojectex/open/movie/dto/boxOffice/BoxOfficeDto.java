package org.spring.backendprojectex.open.movie.dto.boxOffice;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoxOfficeDto {
    private Long id;
    private String movieCd;
    private String rank;
    private String movieNm;
    private String openDt;
    private String audiAcc;
    private String salesAcc;
    private String boxofficeType;
    private String showRange;
    private String yearWeekTime;

}
