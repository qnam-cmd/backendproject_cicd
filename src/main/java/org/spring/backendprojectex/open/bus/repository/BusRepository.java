package org.spring.backendprojectex.open.bus.repository;


import org.spring.backendprojectex.open.bus.entity.BusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<BusEntity,Long> {
    Optional<BusEntity> findByBusRouteId(String busRouteId);
    List<BusEntity> findByBusRouteNmContaining(String searchVal);
}
