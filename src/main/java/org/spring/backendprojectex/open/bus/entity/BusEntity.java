package org.spring.backendprojectex.open.bus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.backendprojectex.open.bus.dto.busList.BusItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bus_tb06")
public class BusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String  busRouteId;     // 버스기본 ID
    private String  busRouteNm;     // 버스 노선
    private String  routeType;      // 타입
    private String  stStationNm;    // 기점
    private String  edStationNm;    // 종점
    private String  firstBusTm;     // 첫차
    private String  lastLowTm;      // 막차
    private String  lastBusTm;
    private String  term;           // 배차시간
    private String  corpNm;         // 버스회차 정보

// 기존 엔티티를 새로운 데이터로 업데이트
    public void updateBusEntity(BusItem item) {
        this.busRouteNm = item.getBusRouteNm();
        this.firstBusTm = item.getFirstBusTm();
        this.lastBusTm = item.getLastBusTm();
        this.lastLowTm = item.getLastLowTm();
        this.term = item.getTerm();
        this.routeType = item.getRouteType();
        this.corpNm = item.getCorpNm();
        this.stStationNm = item.getStStationNm();
        this.edStationNm = item.getEdStationNm();
    }
}
