package org.spring.backendprojectex.open.bus.dto.busList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusItem {
    private String busRouteId;
    private String busRouteNm;
    private String busRouteAbrv;
    private String length;
    private String routeType;      // 타입
    private String stStationNm;    // 기점
    private String edStationNm;    // 종점
    private String term;           // 배차시간
    private String lastBusYn;
    private String firstBusTm;     // 첫차
    private String lastBusTm;
    private String lastLowTm;      // 막차
    private String firstLowTm;
    private String corpNm;         // 버스회차 정보
}
