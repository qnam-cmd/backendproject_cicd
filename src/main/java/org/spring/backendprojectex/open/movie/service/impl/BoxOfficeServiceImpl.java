package org.spring.backendprojectex.open.movie.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.open.movie.dto.boxOffice.BoxOfficeDto;
import org.spring.backendprojectex.open.movie.dto.boxOffice.BoxOfficeResponse;
import org.spring.backendprojectex.open.movie.dto.boxOffice.BoxOfficeResult;
import org.spring.backendprojectex.open.movie.dto.boxOffice.WeeklyBoxOfficeList;
import org.spring.backendprojectex.open.movie.entity.BoxOfficeEntity;
import org.spring.backendprojectex.open.movie.repository.BoxOfficeRepository;
import org.spring.backendprojectex.open.movie.service.BoxOfficeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxOfficeServiceImpl implements BoxOfficeService {

    private final BoxOfficeRepository boxOfficeRepository;

    @Override
    public List<BoxOfficeDto> insertBoxOfficeBody(String boxOfficeBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        BoxOfficeResponse responseWrapper = null;
        try {
            // JSON 전체를 감싸는 최상위 DTO로 파싱
            responseWrapper = objectMapper.readValue(boxOfficeBody, BoxOfficeResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("API 응답 파싱 실패");
        }
        BoxOfficeResult response = responseWrapper.getBoxOfficeResult();
        if(response== null || response.getWeeklyBoxOfficeList()==null) {
            throw new RuntimeException("박스오피스 데이터가 없습니다.");
        }
        // DB X테이블 저장
        List<WeeklyBoxOfficeList> boxOfficeList = response.getWeeklyBoxOfficeList();
        List<BoxOfficeDto> resultDtos = new ArrayList<>();
        for(WeeklyBoxOfficeList item : boxOfficeList) {
            if(boxOfficeRepository.findByMovieCd(item.getMovieCd()).isEmpty()) {
                BoxOfficeEntity entity = BoxOfficeEntity.builder()
                        .movieNm(item.getMovieNm())
                        .movieCd(item.getMovieCd())
                        .rank(item.getRank())
                        .openDt(item.getOpenDt())
                        .audiAcc(item.getAudiAcc())
                        .salesAcc(item.getSalesAcc())
                        .build();
                boxOfficeRepository.save(entity);
            }
            BoxOfficeDto dto = BoxOfficeDto.builder()
                    .movieCd(item.getMovieCd())
                    .movieNm(item.getMovieNm())
                    .rank(item.getRank())
                    .openDt(item.getOpenDt())
                    .audiAcc(item.getAudiAcc())
                    .salesAcc(item.getSalesAcc())
                    .boxofficeType(response.getBoxofficeType() != null ? response.getBoxofficeType().getBoxofficeType() : "")
                    .showRange(response.getShowRange() != null ? response.getShowRange().getShowRange() : "")
                    .yearWeekTime(response.getYearWeekTime() != null ? response.getYearWeekTime().getYearWeekTime() : "")
                    .build();
            resultDtos.add(dto);
        }
        return resultDtos;
    }

    @Override
    public BoxOfficeDto boxOfficeMovieInfoJava(String movieCd) {
        List<BoxOfficeEntity> boxOfficeEntities = boxOfficeRepository.findByMovieCd(movieCd);
        if (boxOfficeEntities.isEmpty()) {
            throw new RuntimeException("박스오피스 데이터가 없습니다.");
        }
        BoxOfficeEntity item = boxOfficeEntities.get(0);    // 박스오피스 조회내용이 있으면
        System.out.println(item + "<< item");
        return BoxOfficeDto.builder()
                .id(item.getId())
                .movieNm(item.getMovieNm())
                .movieCd(item.getMovieCd())
                .rank(item.getRank())
                .openDt(item.getOpenDt())
                .audiAcc(item.getAudiAcc())
                .salesAcc(item.getSalesAcc())
                .build();
    }
}
