package org.spring.backendprojectex.open.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movie_tb06")
public class MovieEntity {
    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String prdtYear;
    private String repGenreNm;
    private String repNationNm;
    private String typeNm;
    @Column(name = "company_cd")
    private String companyCd;
    @Column(name = "company_nm")
    private String companyNm;
    private String openDt;
    private String genreAlt;
    private String prdtStatNm;
    private String source;
    private String totCnt;
}
