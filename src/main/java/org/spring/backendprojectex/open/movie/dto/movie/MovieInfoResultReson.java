package org.spring.backendprojectex.open.movie.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieInfoResultReson {

  private MovieInfoResult movieInfoResult;
}
