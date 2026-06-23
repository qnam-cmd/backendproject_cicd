package org.spring.backendprojectex.open.bus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bus_detail_tb06")
public class BusStationEntity {
    @Id
    @Column(name = "bus_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
