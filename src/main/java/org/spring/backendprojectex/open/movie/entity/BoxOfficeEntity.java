package org.spring.backendprojectex.open.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "boxOffice_tb06")
public class BoxOfficeEntity {
    @Id
    @Column(name = "boxOffice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String movieCd;

    @Column(name = "movie_rank")
    private String rank;

    private String movieNm;
    private String openDt;
    private String audiAcc;
    private String salesAcc;
}
