package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusStationDto {
    private Long id;

    private String busRouteId;

    private String beginTm;
    private String lastTm;
    private String busRouteNm;
    private String routeType;
    private String gpsX;    // 지도 맵연동
    private String gpsY;    // 지도 맵연동
    private String posX;
    private String posY;
    private String stationNm;
    private String stationNo;
}
