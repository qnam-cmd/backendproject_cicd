package org.spring.backendprojectex.open.movie.dto.boxOffice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // 이 부분을 추가하세요!
public class BoxOfficeResponse {
    private BoxOfficeResult boxOfficeResult;
}
