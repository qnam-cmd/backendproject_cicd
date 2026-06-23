package org.spring.backendprojectex.open.movie.dto.boxOffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyBoxOfficeList {
    private String audiAcc;
    private String audiChange;
    private String audiCnt;
    private String audiInten;
    private String movieCd;
    private String movieNm;
    private String openDt;
    private String rank;
    private String rankInten;
    private String rankOldAndNew;
    private String rnum;
    private String salesAcc;
    private String salesAmt;
    private String salesChange;
    private String salesInten;
    private String salesShare;
    private String scrnCnt;
    private String showCnt;

}
