package org.spring.backendprojectex.open.bus.service;


import org.spring.backendprojectex.open.bus.dto.busStation.BusStationDto;

import java.util.List;

public interface BusStationService {
    public void insertBusStations(String busStationList);
    List<BusStationDto> busStationListFn(String busRouteId);
}
