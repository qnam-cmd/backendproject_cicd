package org.spring.backendprojectex.open.bus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.bus.dto.busList.BusDto;
import org.spring.backendprojectex.open.bus.dto.busStation.BusStationDto;
import org.spring.backendprojectex.open.bus.service.BusService;
import org.spring.backendprojectex.open.bus.service.BusStationService;
import org.spring.backendprojectex.open.util.OpenApiBusUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/open/api/bus")
@RequiredArgsConstructor
@Log4j2
public class OpenApiBusRestController {
    private final BusService busService;
    private final BusStationService busStationService;

    @Value("${open.data.bus.serviceKey}")
    private String key;

    @GetMapping("/busList")
    public ResponseEntity<?> busList(@RequestParam(required = false)String searchVal) throws IOException {
        // 공공데이터 API호출
        String busListResult = OpenApiBusUtil.getBusList(key, searchVal);
        log.info("==" + busListResult + "==");
        // DB저장
        busService.insertBusList(busListResult);
        // DB저장 정보 get
        List<BusDto> busList2 = busService.busListFn(searchVal);
        // 반환
        Map<String ,Object> busMap = new HashMap<>();
        busMap.put("busList",busList2);
        return ResponseEntity.status(HttpStatus.OK).body(busMap);
    }

    // 상세목록 DB저장 및 조회
    @GetMapping("/stationList")
    public ResponseEntity<?> stationList(@RequestParam(name = "busRouteId") String busRouteId) {
        log.info(">>>> stationList 컨트롤러 호출됨! 전달된 busRouteId: " + busRouteId);
        try {
            // 1. 공공데이터 API 호출
            String responseBody = OpenApiBusUtil.getStationList(key, busRouteId);
            log.info("정류장 API 응답 수신 완료");

            // 2. 서비스에 JSON 문자열을 넘겨주어 DB에 저장 (이 부분이 누락되어 있었습니다!)
            busStationService.insertBusStations(responseBody);

            // 3. DB에 저장된 정류장 목록 조회 후 반환
            List<BusStationDto> stationList = busStationService.busStationListFn(busRouteId);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("stationList", stationList);
            return ResponseEntity.ok(resultMap);

        } catch (Exception e) {
            log.error("정류장 정보 처리 중 오류 발생: ", e);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "정류장 데이터를 불러오거나 저장하는데 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

}
