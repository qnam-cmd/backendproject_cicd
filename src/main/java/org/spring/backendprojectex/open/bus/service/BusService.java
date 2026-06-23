package org.spring.backendprojectex.open.bus.service;

import org.spring.backendprojectex.open.bus.dto.busList.BusDto;

import java.util.List;

public interface BusService {
    void insertBusList(String busListResult);
    List<BusDto> busListFn(String searchVal);
}
