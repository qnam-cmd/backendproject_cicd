package org.spring.backendprojectex.open.movie.dto.boxOffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxOfficeResult {
    private BoxOfficeType boxofficeType;
    private ShowRange  showRange;
    private List<WeeklyBoxOfficeList> weeklyBoxOfficeList;
    private YearWeekTime yearWeekTime;
}
