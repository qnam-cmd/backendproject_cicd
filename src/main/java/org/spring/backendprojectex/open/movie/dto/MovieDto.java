package org.spring.backendprojectex.open.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "영화진흥회API openAPI 연동을 위한 데이터전송객체(DTO)")
public class MovieDto {

    @Schema(description = "DB저장 아이디",example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String prdtYear;
    private String repGenreNm;
    private String repNationNm;
    private String typeNm;
    private String companyCd;
    private String companyNm;
    private String openDt;
    private String genreAlt;
    private String prdtStatNm;
    private String source;
    private String totCnt;
}
