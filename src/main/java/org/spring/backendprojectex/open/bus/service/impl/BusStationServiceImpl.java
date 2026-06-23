package org.spring.backendprojectex.open.bus.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.bus.dto.busStation.BusItemStation;
import org.spring.backendprojectex.open.bus.dto.busStation.BusStationDto;
import org.spring.backendprojectex.open.bus.dto.busStation.BusStationResultResponse;
import org.spring.backendprojectex.open.bus.entity.BusStationEntity;
import org.spring.backendprojectex.open.bus.repository.BusStationRepository;
import org.spring.backendprojectex.open.bus.service.BusStationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BusStationServiceImpl implements BusStationService {
    private final ObjectMapper objectMapper;
    private final BusStationRepository busStationRepository;



    @Override
    public void insertBusStations(String busStationList) {
        BusStationResultResponse busStationResultResponse = null;
        try {
            busStationResultResponse = objectMapper.readValue(busStationList, BusStationResultResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<BusItemStation> busItemStations = busStationResultResponse.getMsgBody().getItemList();
        System.out.println(busItemStations);
        for (BusItemStation item: busItemStations) {
            BusStationEntity busDetailEntity = BusStationEntity.builder()
                    .busRouteId(item.getBusRouteId())   .beginTm(item.getBeginTm())
                    .lastTm(item.getLastTm())           .busRouteNm(item.getBusRouteNm())
                    .routeType(item.getRouteType())     .gpsX(item.getGpsX())
                    .gpsY(item.getGpsY())               .posX(item.getPosX())
                    .posY(item.getPosY())               .stationNm(item.getStationNm())
                    .stationNo(item.getStationNo())
                    .build();
            Optional<BusStationEntity> optionalBusStationEntity =
                    busStationRepository.findByBusRouteIdAndStationNo(item.getBusRouteId(), item.getStationNo());
            if(optionalBusStationEntity.isEmpty()) {
                busStationRepository.save(busDetailEntity);
            }
        }
    }




    @Override
    public List<BusStationDto> busStationListFn(String busRouteId) {
        List<BusStationEntity> busStationEntities = busStationRepository.findByBusRouteId(busRouteId);
        return busStationEntities.stream().map(bus-> BusStationDto.builder()
                .id(bus.getId())
                .busRouteId(bus.getBusRouteId())        .busRouteNm(bus.getBusRouteNm())
                .gpsX(bus.getGpsX())                    .gpsY(bus.getGpsY())
                .stationNm(bus.getStationNm())          .routeType(bus.getRouteType())
                .build()).collect(Collectors.toList());
    }
}
