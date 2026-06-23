package org.spring.backendprojectex.open.movie.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieInfo {

  private List<Actor> actors;
  private List<Audit> audits;
  private List<CompanyItem> companys;
  private List<DirectorItem> directors;
  private List<GenresItem> genres;
  private String movieCd;
  private String movieNm;
  private String movieNmEn;
  private String movieNmOg;
  private List<Nations> nations;
  private String openDt;
  private String prdtStatNm;
  private String prdtYear;
  private String showTm;
  private List<ShowTypesItem> showTypes;
  private List<StaffsItem> staffs;
  private String typeNm;
  private String source;


}
