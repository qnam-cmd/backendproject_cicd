package org.spring.backendprojectex.open.bus.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.bus.dto.busList.BusDto;
import org.spring.backendprojectex.open.bus.dto.busList.BusItem;
import org.spring.backendprojectex.open.bus.dto.busList.BusListResultResponse;
import org.spring.backendprojectex.open.bus.entity.BusEntity;
import org.spring.backendprojectex.open.bus.repository.BusRepository;
import org.spring.backendprojectex.open.bus.service.BusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void insertBusList(String busListResult) {
        try{
            // JSON -> JAVA 변환
            BusListResultResponse busListResultResponse = objectMapper.readValue(busListResult, BusListResultResponse.class);
            List<BusItem> busItemList = busListResultResponse
                    .getMsgBody()
                    .getItemList();
            System.out.println(busItemList.get(0).getBusRouteId()+" ID");
            for(BusItem busItem: busItemList) {
                Optional<BusEntity> optionalBusEntity = busRepository.findByBusRouteId(busItem.getBusRouteId());
                // 중재하지 않을 때만 INSERT
                if (optionalBusEntity.isEmpty()) {
                    BusEntity busEntity = BusEntity.builder()
                            .busRouteId(busItem.getBusRouteId())
                            .busRouteNm(busItem.getBusRouteNm())
                            .firstBusTm(busItem.getFirstBusTm())
                            .lastBusTm(busItem.getLastBusTm())
                            .lastLowTm(busItem.getFirstLowTm())
                            .term(busItem.getTerm())
                            .routeType(busItem.getRouteType())
                            .corpNm(busItem.getCorpNm())
                            .stStationNm(busItem.getStStationNm())
                            .edStationNm(busItem.getEdStationNm())
                            .build();
                    busRepository.save(busEntity);
                    log.debug("신규 버스 노선 저장: {}", busItem.getBusRouteNm());
                } else {
                    // 존재시 update (선택사항)
                    BusEntity busEntity =  optionalBusEntity.get();
                    busEntity.updateBusEntity(busItem);
                    log.debug("기존 버스 노선 업데이트: {}", busItem.getBusRouteNm());
                }
            }
        } catch (Exception e) {
            log.error("버스 리스트 파싱 또는 저장 중 오류 발생:{}", e.getMessage(),e);
            throw new RuntimeException("버스 데이터 처리 중 오류가 발생했습니다.",e);
        }
    }

    @Override
    public List<BusDto> busListFn(String searchVal) {
        List<BusEntity> busEntities = busRepository.findByBusRouteNmContaining(searchVal);
        System.out.println(busEntities);

        return busEntities.stream().map(bus -> BusDto.builder()
                .id(bus.getId())
                .busRouteId(bus.getBusRouteId())
                .busRouteNm(bus.getBusRouteNm())
                .corpNm(bus.getCorpNm())
                .edStationNm(bus.getEdStationNm())
                .firstBusTm(bus.getFirstBusTm())
                .lastBusTm(bus.getLastBusTm())
                .routeType(bus.getRouteType())
                .stStationNm(bus.getStStationNm())
                .term(bus.getTerm())
                .lastLowTm(bus.getLastLowTm())
                .build()).collect(Collectors.toList());

    }
}


