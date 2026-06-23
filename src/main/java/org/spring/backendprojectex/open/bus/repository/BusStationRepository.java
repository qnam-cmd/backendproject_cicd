package org.spring.backendprojectex.open.bus.repository;

import org.spring.backendprojectex.open.bus.entity.BusStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusStationRepository extends JpaRepository<BusStationEntity,Long> {
    List<BusStationEntity> findByBusRouteId(String busRouteId);
    Optional<BusStationEntity> findByStationNo(String stationNo);
    Optional<BusStationEntity> findByBusRouteIdAndStationNo(String busRouteId,String stationNo);
}
